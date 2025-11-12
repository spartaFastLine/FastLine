package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.service.DeliveryManagerService;
import com.fastline.authservice.presentation.dto.request.DeliveryManagerCreateRequest;
import com.fastline.authservice.presentation.dto.request.DeliveryManagerDeleteRequest;
import com.fastline.authservice.presentation.dto.request.DeliveryManagerSearchRequest;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerAssignResponse;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerResponse;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.success.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/delivery/managers")
@RequiredArgsConstructor
public class DeliveryManagerController {

	private final DeliveryManagerService deliveryManagerService;

	// 배달 매니저 생성 - 마스터, 허브 관리자만 가능
	@PostMapping()
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> createDeliveryManager(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid DeliveryManagerCreateRequest requestDto) {
		deliveryManagerService.createDeliveryManager(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_CREATE_SUCCESS);
	}

	// 배달 매니저 단건 조회 - 배달 매니저 본인만 가능, 마스터나 허브 관리자는 다건 조회에서 가능
	@GetMapping()
	@PreAuthorize("hasAnyRole('DELIVERY_MANAGER')")
	public ResponseEntity<ApiResponse<DeliveryManagerResponse>> getDeliveryManager(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeliveryManagerResponse responseDto =
				deliveryManagerService.getDeliveryManager(userDetails.getUserId());
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_READ_SUCCESS, responseDto);
	}

	// 다건 조회 - 마스터, 허브 관리자만 가능
	@GetMapping("/managers")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Page<DeliveryManagerResponse>>> getDeliveryManagers(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid DeliveryManagerSearchRequest requestDto) {
		Page<DeliveryManagerResponse> responseDto =
				deliveryManagerService.getDeliveryManagers(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_READ_SUCCESS, responseDto);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> updateDeliveryManager(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid DeliveryManagerCreateRequest requestDto) {
		deliveryManagerService.updateDeliveryManager(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_UPDATE_SUCCESS);
	}

	@DeleteMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> deleteDeliveryManager(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid DeliveryManagerDeleteRequest requestDto) {
		deliveryManagerService.deleteDeliveryManager(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_DELETE_SUCCESS);
	}

	// 배달 매니저 자동 배정
	@GetMapping("/assign/{hubId}/{managerType}")
	public ResponseEntity<ApiResponse<DeliveryManagerAssignResponse>> getDeliveryManagerAssignment(
			@PathVariable String hubId, @PathVariable String managerType) {
		DeliveryManagerAssignResponse responseDto =
				deliveryManagerService.getDeliveryManagerAssignment(hubId, managerType);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_ASSIGN_SUCCESS, responseDto);
	}

	// 배달매니저 배송완료 알림
	@PostMapping("/{managerId}/complete")
	public ResponseEntity<ApiResponse<Void>> completeDeliveryManager(@PathVariable Long managerId) {
		deliveryManagerService.completeDeliveryManager(managerId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_COMPLETE_SUCCESS);
	}
}
