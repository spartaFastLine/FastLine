package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.security.UserDetailsImpl;
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

	// 유저 다건 조회
	@PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_HUB_MANAGER')")
	@GetMapping("/managers/users")
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
	@PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_HUB_MANAGER')")
	@DeleteMapping("/managers/withdraw/permit")
	public ResponseEntity<ApiResponse<Void>> deleteUserpermit(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid PermitRequestDto requestDto) {
		userService.deleteUserpermit(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_DELETE_SUCCESS);
	}

	// 후에 회원 강제 탈퇴기능 추가
}
