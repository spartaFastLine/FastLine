package com.fastline.authservice.presentation.controller;

import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.authservice.domain.service.UserService;
import com.fastline.authservice.presentation.request.*;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
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
	@PutMapping("/permit/signup")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> permitSignup(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid PermitRequestDto requestDto) {
		userService.permitSignup(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_PERMIT_SUCCESS);
	}

	// 유저 다건 조회
	@GetMapping("/managers/users")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Page<UserResponseDto>>> getUsers(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody UserSearchRequestDto requestDto) {
		Page<UserResponseDto> responseDto =
				userService.getUsers(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);
	}

	// 유저 단건 조회- 전체 가능
	@GetMapping("/user")
	public ResponseEntity<ApiResponse<UserResponseDto>> getUser(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UserResponseDto responseDto = userService.getUser(userDetails.getUserId());
		return ResponseUtil.successResponse(SuccessCode.USER_READ_SUCCESS, responseDto);
	}

	// 비밀번호 수정- 전체 가능
	@PutMapping("/user/password")
	public ResponseEntity<ApiResponse<Void>> updatePassword(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdatePasswordRequestDto requestDto) {
		userService.updatePassword(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.PASSWORD_UPDATE_SUCCESS);
	}

	// 슬랙 아이디 수정- 전체 가능
	@PutMapping("/user/slack")
	public ResponseEntity<ApiResponse<Void>> updateUserSlack(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid UpdateSlackRequestDto requestDto) {
		userService.updateSlack(userDetails.getUserId(), requestDto);
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
	@DeleteMapping("/managers/withdraw/permit")
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> deleteUserpermit(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid PermitRequestDto requestDto) {
		userService.permitDeleteUser(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_DELETE_SUCCESS);
	}

	// 후에 회원 강제 탈퇴기능 추가
}
