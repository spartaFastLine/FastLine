package com.fastline.hubservice.application.service;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.model.HubPath;
import com.fastline.hubservice.domain.repository.HubPathRepository;
import com.fastline.hubservice.domain.repository.HubRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubGraphService {

	public static final String HUB_GRAPH_CACHE = "hubGraph";

	private final HubRepository hubRepository;
	private final HubPathRepository hubPathRepository;

	@Transactional
	@Cacheable(value = HUB_GRAPH_CACHE, key = "'v1'")
	public Graph buildGraph() {
		// 1) 활성 허브 로딩 (deletedAt IS NULL 등 조건은 repo가 보장)
		List<Hub> hubs = hubRepository.findAllActive();
		if (hubs.isEmpty()) {
			throw new CustomException(ErrorCode.NO_ACTIVE_HUBS);
		}

		// ID -> 허브 맵
		Map<UUID, Hub> hubMap =
				hubs.stream().collect(Collectors.toMap(Hub::getHubId, Function.identity()));

		// 2) 활성 경로 로딩
		List<HubPath> paths = hubPathRepository.findAllActive();
		Graph g = new Graph(); // Jackson 친화형(Graph, Edge는 이전 답변 코드 참고)

		for (HubPath p : paths) {
			// 안전 가드: 시작/도착 허브가 모두 활성 목록 안에 존재해야 함
			UUID startId = p.getStartHub().getHubId();
			UUID endId = p.getEndHub().getHubId();
			if (!hubMap.containsKey(startId) || !hubMap.containsKey(endId)) {
				// 고아 엣지 스킵
				continue;
			}

			// 거리/시간 변환
			Double distance = null;
			if (p.getDistance() != null) {
				// BigDecimal -> Double
				distance = p.getDistance().doubleValue();
			}

			Integer durationMin = null;
			if (p.getDuration() != null) {
				// LocalTime 을 분 단위로 환산 (00:00 기준 경과분)
				durationMin = p.getDuration().getHour() * 60 + p.getDuration().getMinute();
			}

			// 둘 다 없으면 최소 보장: 허브 좌표로 해버사인 거리 계산해 대체(시간은 null 허용)
			if (distance == null && durationMin == null) {
				Hub a = hubMap.get(startId);
				Hub b = hubMap.get(endId);
				if (a.getLatitude() != null
						&& a.getLongitude() != null
						&& b.getLatitude() != null
						&& b.getLongitude() != null) {
					distance =
							haversineKm(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
				} else {
					// 좌표도 없으면 스킵(깨진 데이터)
					continue;
				}
			}

			// 3) 양방향 엣지 추가 (Hub&Spoke라도 허브-허브 간 경로는 보통 왕복 가능)
			Graph.Edge forward = new Graph.Edge(endId, distance, durationMin, p.getHubPathId());
			g.addEdge(startId, forward);

			Graph.Edge backward = new Graph.Edge(startId, distance, durationMin, p.getHubPathId());
			g.addEdge(endId, backward);
		}

		return g;
	}

	// 해버사인 거리(km)
	private static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
		final double R = 6371.0088; // km
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double sLat1 = Math.toRadians(lat1);
		double sLat2 = Math.toRadians(lat2);

		double a =
				Math.pow(Math.sin(dLat / 2), 2)
						+ Math.pow(Math.sin(dLon / 2), 2) * Math.cos(sLat1) * Math.cos(sLat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}
}
