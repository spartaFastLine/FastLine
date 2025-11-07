package com.fastline.hubservice.presentation.controller;

import com.fastline.hubservice.application.command.HubSearchCommand;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.presentation.request.HubListRequest;
import com.fastline.hubservice.presentation.response.HubGetResponse;
import com.fastline.hubservice.presentation.response.HubListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastline.hubservice.application.command.CreateHubCommand;
import com.fastline.hubservice.application.service.HubService;
import com.fastline.hubservice.presentation.ApiResponse;
import com.fastline.hubservice.presentation.request.CreateHubRequest;
import com.fastline.hubservice.presentation.response.HubCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/hub")
@RequiredArgsConstructor
@Validated
public class HubController {

    @GetMapping("/hub")
    public String hello() {
        return "Hello World";
    }
    private final HubService hubService;
    /**
     * 허브 생성
     * POST /api/hubs
     */
    @PostMapping()
    public ResponseEntity<ApiResponse<HubCreateResponse>> CreateHub(
            @Valid @RequestBody CreateHubRequest request) {

        CreateHubCommand command = new CreateHubCommand(
                request.getCentralHubId(),
                request.getIsCentral(),
                request.getName(),
                request.getAddress(),
                request.getLatitude(),
                request.getLongitude()
        );

        // 2. 애플리케이션 서비스 호출
        UUID hubId = hubService.createHub(command);

        // 3. 응답 생성
        HubCreateResponse response = new HubCreateResponse(
                hubId,
                "허브가 성공적으로 생성되었습니다"
        );
        log.info("허브 생성 성공: hub_id={}", hubId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    /**
     * 허브 단건 조회
     * GET /api/hub/{hubId}
     */
    @GetMapping("/{hubId}")
    public ResponseEntity<ApiResponse<HubGetResponse>> getHub(@PathVariable("hubId") UUID hubId) {
        Hub hub = hubService.getHub(hubId); // 애플리케이션 서비스에서 조회 (미구현 시 추가 필요)
        HubGetResponse response = HubGetResponse.from(hub);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * 허브 다건 조회 (필터 + 페이지네이션)
     * GET /api/hubs
     *
     * Query Params:
     * - name:      부분 일치 검색
     * - address:   부분 일치 검색
     * - centralHubId: 상위(중앙) 허브 ID
     * - isCentral: 중앙 허브 여부
     * - page, size, sort: 스프링 기본 페이지네이션 파라미터
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<HubListResponse>>> listHubs(
            @RequestBody HubListRequest request,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        HubSearchCommand command = new HubSearchCommand(
                request.getName(),
                request.getAddress(),
                request.getCentralHubId(),
                request.getIsCentral()
        );
        Page<Hub> page = hubService.searchHubs(command, pageable);
        Page<HubListResponse> body = page.map(HubListResponse::from);
        return ResponseEntity.ok(ApiResponse.success(body));
    }

    /**
     * 허브 소프트 삭제
     * DELETE /api/hub/{hubId}
     *
     * 허브를 실제로 삭제하지 않고 deletedAt 컬럼에 시간 값을 세팅합니다.
     */
    @DeleteMapping("/{hubId}")
    public ResponseEntity<ApiResponse<String>> deleteHub(@PathVariable("hubId") UUID hubId) {
        hubService.softDeleteHub(hubId);
        log.info("허브 소프트 삭제 완료: hub_id={}", hubId);
        return ResponseEntity.ok(ApiResponse.success("허브가 소프트 삭제되었습니다."));
    }
}
