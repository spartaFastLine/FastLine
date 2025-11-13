package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.application.command.UpdatePasswordCommand;
import com.fastline.authservice.application.command.UpdateSlackCommand;
import com.fastline.authservice.application.command.UserSearchCommand;
import com.fastline.authservice.application.result.DeliveryManagerMessageResult;
import com.fastline.authservice.application.result.UserHubIdResult;
import com.fastline.authservice.application.result.UserResult;
import com.fastline.authservice.application.service.UserService;
import com.fastline.authservice.presentation.dto.request.UpdatePasswordRequest;
import com.fastline.authservice.presentation.dto.request.UpdateSlackRequest;
import com.fastline.authservice.presentation.dto.request.UserSearchRequest;
import com.fastline.authservice.presentation.dto.response.DeliveryManagerMessageResponse;
import com.fastline.authservice.presentation.dto.response.UserHubIdResponse;
import com.fastline.authservice.presentation.dto.response.UserResponse;
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
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PathVariable("userId") Long userId){
		userService.permitSignup(userDetails.getUserId(), userId);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_PERMIT_SUCCESS);
	}

	// 유저 다건 조회
	@GetMapping("/managers/users")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody UserSearchRequest requestDto) {
		UserSearchCommand command = new UserSearchCommand(
				requestDto.page(),
				requestDto.size(),
				requestDto.hubId(),
				requestDto.username(),
				requestDto.role(),
				requestDto.status(),
				requestDto.sortBy(),
				requestDto.sortAscending()
		);
		Page<UserResult> results = userService.getUsers(userDetails.getUserId(), command);
		Page<UserResponse> responseDto = results.map(
				result -> new UserResponse(
						result.userId(),
						result.email(),
						result.username(),
						result.role(),
						result.slackId(),
						result.status(),
						result.hubId()
				)
		);

		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);
	}

	// 유저 단건 조회- 전체 가능
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<UserResponse>> getUser(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UserResult result = userService.getUser(userDetails.getUserId());
		UserResponse response = new UserResponse(
				result.userId(),
				result.email(),
				result.username(),
				result.role(),
				result.slackId(),
				result.status(),
				result.hubId()
		);
		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, response);
	}

	// 비밀번호 수정- 전체 가능
	@PutMapping("/user/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdatePasswordRequest requestDto) {
		UpdatePasswordCommand command = new UpdatePasswordCommand(
				requestDto.password(),
				requestDto.newPassword()
		);
		userService.updatePassword(userDetails.getUserId(), command);
		return ResponseUtil.successResponse(SuccessCode.PASSWORD_UPDATE_SUCCESS);
	}

	// 슬랙 아이디 수정- 전체 가능
	@PutMapping("/user/slack")
	public ResponseEntity<ApiResponse<Void>> updateUserSlack(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdateSlackRequest requestDto) {
		UpdateSlackCommand command = new UpdateSlackCommand(
				requestDto.slackId()
		);
		userService.updateSlack(userDetails.getUserId(), command);
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
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@PathVariable("userId") Long userId) {
		userService.permitDeleteUser(userDetails.getUserId(), userId);
		return ResponseUtil.successResponse(SuccessCode.USER_DELETE_SUCCESS);
	}

	// 내부요청

	// 배송담당자 정보 조회
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse<DeliveryManagerMessageResponse>> getDeliveryManagerMessageInfo(
			@PathVariable Long userId) {
		DeliveryManagerMessageResult result = userService.getDeliveryManagerMessageInfo(userId);
		DeliveryManagerMessageResponse responseDto = new DeliveryManagerMessageResponse(
				result.slackId(),
				result.username(),
				result.email()
		);
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
}
