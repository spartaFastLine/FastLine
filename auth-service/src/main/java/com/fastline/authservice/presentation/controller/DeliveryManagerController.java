package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.service.DeliveryManagerService;
import com.fastline.authservice.presentation.request.DeliveryManagerCreateRequestDto;
import com.fastline.authservice.presentation.request.DeliveryManagerResponseDto;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.success.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

    private final DeliveryManagerService deliveryManagerService;

    //배달 매니저 생성 - 마스터, 허브 관리자만 가능
    @PostMapping()
    @PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> createDeliveryManager(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid DeliveryManagerCreateRequestDto requestDto) {
        deliveryManagerService.createDeliveryManager(userDetails, requestDto);
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_CREATE_SUCCESS);
    }

    //배달 매니저 단건 조회 - 배달 매니저 본인만 가능, 마스터나 허브 관리자는 다건 조회에서 가능
    @GetMapping()
    @PreAuthorize("hasAnyRole('DELIVERY_MANAGER')")
    public ResponseEntity<ApiResponse<DeliveryManagerResponseDto>> getDeliveryManager(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        DeliveryManagerResponseDto responseDto = deliveryManagerService.getDeliveryManager(userDetails.getUserId());
        return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_READ_SUCCESS, responseDto);
    }
}
