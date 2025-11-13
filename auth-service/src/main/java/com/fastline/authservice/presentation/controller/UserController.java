package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.application.command.*;
import com.fastline.authservice.application.result.*;
import com.fastline.authservice.application.service.UserService;
import com.fastline.authservice.presentation.dto.request.*;
import com.fastline.authservice.presentation.dto.response.*;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// 회원가입 승인
	@PutMapping("/permit/signup/{userId}")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> permitSignup(
			@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("userId") Long userId) {
		userService.permitSignup(userDetails.getUserId(), userId);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_PERMIT_SUCCESS);
	}

	// 유저 다건 조회
	@GetMapping("/managers/users")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody UserSearchRequest requestDto) {
		UserSearchCommand command =
				new UserSearchCommand(
						requestDto.page(),
						requestDto.size(),
						requestDto.hubId(),
						requestDto.username(),
						requestDto.role(),
						requestDto.status(),
						requestDto.sortBy(),
						requestDto.sortAscending());
		Page<UserResult> results = userService.getUsers(userDetails.getUserId(), command);
		Page<UserResponse> responseDto =
				results.map(
						result ->
								new UserResponse(
										result.userId(),
										result.email(),
										result.username(),
										result.role(),
										result.slackId(),
										result.status(),
										result.hubId(),
										result.deliveryType(),
										result.deliveryNumber()));

		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);
	}

	// 다건 조회 - 마스터, 허브 관리자만 가능
	@GetMapping("/managers")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Page<DeliveryManagerResponse>>> getDeliveryManagers(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid DeliveryManagerSearchRequest request) {
		DeliveryManagerCommand command =
				new DeliveryManagerCommand(
						request.page(),
						request.size(),
						request.username(),
						request.hubId(),
						request.type(),
						request.number(),
						request.status(),
						request.isActive(),
						request.sortBy(),
						request.sortAscending());
		Page<DeliveryManagerResult> results =
				userService.getDeliveryManagers(userDetails.getUserId(), command);
		Page<DeliveryManagerResponse> response =
				results.map(
						result ->
								new DeliveryManagerResponse(
										result.userId(),
										result.username(),
										result.slackId(),
										result.hubId(),
										result.deliveryType(),
										result.deliveryNumber()));

		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_READ_SUCCESS, response);
	}

	// 유저 단건 조회- 전체 가능
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<UserResponse>> getUser(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UserResult result = userService.getUser(userDetails.getUserId());
		UserResponse response =
				new UserResponse(
						result.userId(),
						result.email(),
						result.username(),
						result.role(),
						result.slackId(),
						result.status(),
						result.hubId(),
						result.deliveryType(),
						result.deliveryNumber());
		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, response);
	}

	// 비밀번호 수정- 전체 가능
	@PutMapping("/user/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdatePasswordRequest requestDto) {
		UpdatePasswordCommand command =
				new UpdatePasswordCommand(requestDto.password(), requestDto.newPassword());
		userService.updatePassword(userDetails.getUserId(), command);
		return ResponseUtil.successResponse(SuccessCode.PASSWORD_UPDATE_SUCCESS);
	}

	// 슬랙 아이디 수정- 전체 가능
	@PutMapping("/user/slack")
	public ResponseEntity<ApiResponse<Void>> updateUserSlack(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdateSlackRequest requestDto) {
		UpdateSlackCommand command = new UpdateSlackCommand(requestDto.slackId());
		userService.updateSlack(userDetails.getUserId(), command);
		return ResponseUtil.successResponse(SuccessCode.USER_UPDATE_SUCCESS);
	}

	// 유저 정보 수정 - 관리자만 가능한 영격
	@PutMapping("/{userId}")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> updateDeliveryManager(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PathVariable("userId") Long userId,
			@RequestBody @Valid UserManagerUpdateRequest request) {
		UserManagerUpdateCommand command =
				new UserManagerUpdateCommand(request.hubId(), request.status(), request.deliveryType());
		userService.updateDeliveryManager(userDetails.getUserId(), userId, command);
		return ResponseUtil.successResponse(SuccessCode.USER_UPDATE_SUCCESS);
	}

	// 회원 탈퇴 신청
	@PostMapping("/user/withdraw")
	public ResponseEntity<ApiResponse<Void>> withdrawUser(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		userService.withdrawUser(userDetails.getUserId());
		return ResponseUtil.successResponse(SuccessCode.USER_WITHDRAWAL_REQUEST_SUCCESS);
	}

	// 회원 탈퇴 승인
	@DeleteMapping("/managers/withdraw/{userId}/permit")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> deleteUserpermit(
			@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("userId") Long userId) {
		userService.permitDeleteUser(userDetails.getUserId(), userId);
		return ResponseUtil.successResponse(SuccessCode.USER_DELETE_SUCCESS);
	}

	// 내부요청

	// 배송담당자 정보 조회
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<DeliveryManagerMessageResponse>> getDeliveryManagerMessageInfo(
			@PathVariable Long userId) {
		DeliveryManagerMessageResult result = userService.getDeliveryManagerMessageInfo(userId);
		DeliveryManagerMessageResponse responseDto =
				new DeliveryManagerMessageResponse(result.slackId(), result.username(), result.email());
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_READ_SUCCESS, responseDto);
	}

	// 배송담당자 허브ID 조회
	@GetMapping("/{userId}/hubId")
	public ResponseEntity<ApiResponse<UserHubIdResponse>> getHubMessageInfo(
			@PathVariable Long userId) {
		UserHubIdResult result = userService.getUserHubInfo(userId);
		UserHubIdResponse responseDto = new UserHubIdResponse(result.hubId());
		return ResponseUtil.successResponse(SuccessCode.HUBID_READ_SUCCESS, responseDto);
	}

	// 배달 매니저 자동 배정
	@GetMapping("/assign/{hubId}/{managerType}")
	public ResponseEntity<ApiResponse<DeliveryManagerAssignResponse>> getDeliveryManagerAssignment(
			@PathVariable String hubId, @PathVariable String managerType) {
		DeliveryManagerAssignResult result =
				userService.getDeliveryManagerAssignment(hubId, managerType);
		DeliveryManagerAssignResponse responseDto =
				new DeliveryManagerAssignResponse(result.managerId());
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_ASSIGN_SUCCESS, responseDto);
	}

	// 배달매니저 배송완료 알림
	@PostMapping("/{managerId}/complete")
	public ResponseEntity<ApiResponse<Void>> completeDeliveryManager(@PathVariable Long managerId) {
		userService.completeDeliveryManager(managerId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_MANAGER_COMPLETE_SUCCESS);
	}
}
