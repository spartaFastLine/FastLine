package com.fastline.hubservice.application.service;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.hubservice.application.command.HubPathCreateCommand;
import com.fastline.hubservice.domain.enums.RouteMetric;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.model.HubPath;
import com.fastline.hubservice.domain.repository.HubPathRepository;
import com.fastline.hubservice.domain.repository.HubRepository;
import com.fastline.hubservice.infrastructure.naver.dto.StopoverDto;
import com.fastline.hubservice.infrastructure.naver.service.NaverService;
import com.fastline.hubservice.presentation.response.HubPathCreateResponse;
import com.fastline.hubservice.presentation.response.HubPathResponse;
import com.fastline.hubservice.presentation.response.OptimalRouteResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubPathService {
    private final HubRepository hubRepository;
    private final HubPathRepository hubPathRepository;
    private final NaverService naverService;
    private final HubGraphService hubGraphService;
    private final ShortestPath shortestPath;

    @Transactional
    @CacheEvict(value = {"hubGraph", "route"}, allEntries = true) // 경로 변경 시 그래프/경로 캐시 무효화
    public HubPathCreateResponse createRoute(HubPathCreateCommand cmd) {
        UUID startId = cmd.getStartHubId();
        UUID endId   = cmd.getEndHubId();

        Hub start = hubRepository.findActiveById(startId)
                .orElseThrow(() -> new CustomException(ErrorCode.START_HUB_NOT_FOUND_OR_DELETED));
        Hub end = hubRepository.findActiveById(endId)
                .orElseThrow(() -> new CustomException(ErrorCode.END_HUB_NOT_FOUND_OR_DELETED));

        // --- createHmi 스타일: 좌표 구성 → Naver API 호출 → 거리/시간 산출 ---
        Long distance = 0L;   // fallback: 요청값 (km 또는 m 기준은 호출부 정책)
        Long durationSec = 0L;   // fallback: 요청값(초 단위 가정)
        LocalTime durationTime = secondsToLocalTime(durationSec);

        try {
            String startCoord = start.getLatitude() + "," + start.getLongitude();
            String endCoord   = end.getLatitude() + "," + end.getLongitude();

            // 경유지 없이 기본 경로 조회 (필요 시 cmd에 따라 확장 가능)
            List<StopoverDto> stopovers = naverService.naverApi(new String[]{startCoord, endCoord}, 0);

            if (stopovers != null && !stopovers.isEmpty()) {
                long totalDist = 0L;
                long totalTime = 0L;
                for (StopoverDto s : stopovers) {
                    if (s.distance() != null) totalDist += s.distance();
                    if (s.duration() != null) totalTime += s.duration();
                }
                // API가 제공하면 API 값을 우선 사용
                distance = totalDist;
                durationTime = secondsToLocalTime(totalTime);
            }
        } catch (Exception e) {
            log.warn("Unexpected error while calling Naver API. Falling back to request metrics. reason={}", e.getMessage());
        }

        // 허브 연결 규칙 검증 후 저장 (거리/시간을 API 결과로 대체)
        return validateAndUpsertWithMetrics(start, end, distance, durationTime, cmd.getActive());
    }


    private HubPathCreateResponse validateAndUpsertWithMetrics(Hub start, Hub end, Long distance, LocalTime duration, Boolean activeFlag) {
        boolean startCentral = start.isCentral();
        boolean endCentral   = end.isCentral();

        // (A) 거점↔거점 : 허용
        if (startCentral && endCentral) {
            return upsertEdgeWithMetrics(start, end, distance, duration, activeFlag);
        }

        // (B) 멤버↔자기 거점 : 허용
        if (!startCentral && endCentral) {
            UUID startCentralId = hubRepository.findCentralIdOf(start.getHubId());
            if (!end.getHubId().equals(startCentralId)) {
                throw new CustomException(ErrorCode.MEMBER_NOT_CONNECTED_TO_ITS_CENTRAL);
            }
            return upsertEdgeWithMetrics(start, end, distance, duration, activeFlag);
        }
        if (startCentral && !endCentral) {
            UUID endCentralId = hubRepository.findCentralIdOf(end.getHubId());
            if (!start.getHubId().equals(endCentralId)) {
                throw new CustomException(ErrorCode.MEMBER_NOT_CONNECTED_TO_ITS_CENTRAL);
            }
            return upsertEdgeWithMetrics(start, end, distance, duration, activeFlag);
        }

        // (C) 멤버↔멤버 : 금지
        throw new CustomException(ErrorCode.MEMBER_TO_MEMBER_NOT_ALLOWED);
    }


    private HubPathCreateResponse upsertEdgeWithMetrics(Hub start, Hub end, Long distance, LocalTime duration, Boolean activeFlag) {
        HubPath edge = hubPathRepository.findActiveByStartAndEnd(start.getHubId(), end.getHubId())
                .orElseGet(() -> HubPath.builder()
                        .startHub(start)
                        .endHub(end)
                        .active(true)
                        .build());

        if (duration != null) {
            edge.setDuration(duration);
        }
        if (distance != null) {
            edge.setDistance(distance);
        }
        edge.setActive(activeFlag != null ? activeFlag : Boolean.TRUE);

        HubPath saved = hubPathRepository.save(edge);
        return HubPathCreateResponse.from(saved);
    }

    private LocalTime secondsToLocalTime(Long seconds) {
        if (seconds == null) return null;
        // If incoming duration is milliseconds, change to `/ 1000`.
        long s = seconds;
        if (s < 0) s = 0;
        // Cap at 24h-1s so it fits LocalTime (00:00:00~23:59:59)
        long daySeconds = 24 * 60 * 60;
        s = s % daySeconds;
        return LocalTime.ofSecondOfDay(s);
    }


    @Transactional
    public HubPathResponse getRoute(UUID hubPathId) {
        HubPath hubPath = hubPathRepository.findByHubPathIdAndDeletedAtIsNull(hubPathId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND_OR_DELETED));

        return HubPathResponse.from(hubPath);
    }

    /**
     * 허브 경로 단건 소프트 삭제
     * - deleted_at, deleted_by 세팅
     * - 캐시 무효화
     */
    @Transactional
    @CacheEvict(value = {"hubGraph", "route"}, allEntries = true)
    public void softDeleteRoute(UUID hubPathId, Long deletedBy) {
        HubPath edge = hubPathRepository.findByHubPathIdAndDeletedAtIsNull(hubPathId)
                .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND_OR_DELETED));

        edge.setActive(false);

        hubPathRepository.save(edge);
    }


    @Transactional
    public OptimalRouteResponse findOptimalRoute(UUID startId, UUID endId, RouteMetric metric) {
        Hub start = hubRepository.findActiveById(startId)
                .orElseThrow(() -> new CustomException(ErrorCode.START_HUB_NOT_FOUND_OR_DELETED));
        Hub end   = hubRepository.findActiveById(endId)
                .orElseThrow(() -> new CustomException(ErrorCode.END_HUB_NOT_FOUND_OR_DELETED));

        // 그래프 확보 (캐시)
        Graph g = hubGraphService.buildGraph();

        // 가중치 미기준(0/INF) 엣지에 대해 네이버 API 보강(선택적)
        ensureEdgeWeightsIfMissing(g, metric);

        // 최단 경로
        List<UUID> nodeIds = shortestPath.dijkstra(g, start.getHubId(), end.getHubId());
        if (nodeIds.isEmpty()) throw new CustomException(ErrorCode.NO_PATH_AVAILABLE);

        // 총합 계산(거리/시간은 DB에서 재합산: 정밀)
        long totalSec = 0L;
        long totalDist = 0L;
        List<HubPath> legs = new ArrayList<>();

        for (int i = 0; i < nodeIds.size()-1; i++) {
            UUID a = nodeIds.get(i);
            UUID b = nodeIds.get(i+1);
            HubPath leg = hubPathRepository
                    .findActiveByStartAndEnd(a, b)
                    .orElseThrow(() -> new CustomException(ErrorCode.ROUTE_NOT_FOUND_OR_DELETED));

            if (leg.getDuration() != null) totalSec += leg.getDuration().toSecondOfDay();
            if (leg.getDistance() != null) totalDist += leg.getDistance();
            legs.add(leg);
        }

        // 응답 DTO
        return OptimalRouteResponse.of(legs, totalDist, totalSec);
    }

    @Transactional
    @CacheEvict(value = {"hubGraph","route"}, allEntries = true)
    public void ensureEdgeWeightsIfMissing(Graph g, RouteMetric metric) {
        // 예시: 그래프 내 가중치가 무한대(또는 0)인 엣지를 찾아 네이버 API로 조회 후 upsert
        for (var entry : g.getAdj().entrySet()) {
            UUID from = entry.getKey();
            for (Graph.Edge e : entry.getValue()) {
                if (Double.isInfinite(e.getDistance()) || e.getDistance() == 0.0) {
                    hubPathRepository.findActiveByStartAndEnd(from, e.getTo()).ifPresent(leg -> {
                        // 좌표
                        Hub s = leg.getStartHub();
                        Hub t = leg.getEndHub();
                        String startCoord = s.getLatitude() + "," + s.getLongitude();
                        String endCoord   = t.getLatitude() + "," + t.getLongitude();

                        try {
                            var stopovers = naverService.naverApi(new String[]{startCoord, endCoord}, 0);
                            long sumDist = 0L, sumSec = 0L;
                            if (stopovers != null) {
                                for (var st : stopovers) {
                                    if (st.distance() != null) sumDist += st.distance();
                                    if (st.duration() != null) sumSec += st.duration();
                                }
                                if (sumDist > 0) leg.setDistance(sumDist);
                                if (sumSec > 0)  leg.setDuration(LocalTime.ofSecondOfDay(sumSec % (24*3600)));
                                hubPathRepository.save(leg);
                            }
                        } catch (Exception ex) {
                            // 실패 시 로깅만; 다음 호출/배치에서 보정
                            log.warn("edge metric fill failed from {} to {}: {}", from, e.getTo(), ex.getMessage());
                        }
                    });
                }
            }
        }
    }
}
