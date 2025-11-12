package com.fastline.hubservice.presentation.controller;

import com.fastline.hubservice.application.command.HubPathCreateCommand;
import com.fastline.hubservice.application.service.HubPathService;
import com.fastline.hubservice.domain.enums.RouteMetric;
import com.fastline.hubservice.presentation.ApisResponse;
import com.fastline.hubservice.presentation.request.HubPathCreateRequest;
import com.fastline.hubservice.presentation.response.HubPathCreateResponse;
import com.fastline.hubservice.presentation.response.HubPathResponse;
import com.fastline.hubservice.presentation.response.OptimalRouteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/hub-path")
@RequiredArgsConstructor
@Validated
@Tag(name = "HubPath", description = "허브 경로 API")
public class HubPathController {

    private final HubPathService hubPathService;

    /**
     * 허브 간 이동정보 등록
     * POST /api/hub-path
     */
    @Operation(
        summary = "허브 경로 생성",
        description = "시작 허브와 도착 허브를 연결하는 경로를 생성합니다.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "허브 경로 생성 요청",
            content = @Content(schema = @Schema(implementation = HubPathCreateRequest.class))
        )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = com.fastline.hubservice.presentation.response.HubPathCreateResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "유효성 오류"
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "409",
            description = "중복 충돌"
        )
    })
    @PostMapping
    public ResponseEntity<ApisResponse<HubPathCreateResponse>> createHubRoute(
            @Valid @RequestBody HubPathCreateRequest request
    ) {
        HubPathCreateCommand command = HubPathCreateCommand.builder()
                .startHubId(request.getStartHubId())
                .endHubId(request.getEndHubId())
                .active(request.getActive())
                .build();

        HubPathCreateResponse response = hubPathService.createRoute(command);
        return ResponseEntity.ok(ApisResponse.success(response));
    }

    @Operation(
        summary = "허브 경로 단건 조회",
        description = "hubPathId로 단건 허브 경로를 조회합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = HubPathResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "경로를 찾을 수 없음"
        )
    })
    @GetMapping("/{hubPathId}")
    public ResponseEntity<ApisResponse<HubPathResponse>> getHubRoute(
            @io.swagger.v3.oas.annotations.Parameter(description = "허브 경로 ID", required = true)
            @PathVariable UUID hubPathId) {
        HubPathResponse response = hubPathService.getRoute(hubPathId);
        return ResponseEntity.ok(ApisResponse.success(response));
    }

    @GetMapping("/optimal")
    public ResponseEntity<ApisResponse<OptimalRouteResponse>> findOptimalRoute(
            @RequestParam UUID startId,
            @RequestParam UUID endId,
            @RequestParam(defaultValue = "TIME") RouteMetric metric // TIME or DISTANCE
    ) {
        OptimalRouteResponse resp = hubPathService.findOptimalRoute(startId, endId, metric);
        return ResponseEntity.ok(ApisResponse.success(resp));
    }

}
