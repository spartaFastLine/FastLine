package com.fastline.hubservice.presentation.controller;


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
}
