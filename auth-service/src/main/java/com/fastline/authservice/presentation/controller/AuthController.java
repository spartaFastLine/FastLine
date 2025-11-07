package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.domain.security.UserDetailsImpl;
import com.fastline.authservice.domain.service.AuthService;
import com.fastline.authservice.presentation.request.LoginRequestDto;
import com.fastline.authservice.presentation.request.PermitRequestDto;
import com.fastline.authservice.presentation.request.SignupRequestDto;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid SignupRequestDto requestDto) {
		authService.signup(requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_SUCCESS);
	}

	// 회원가입 승인
	@PutMapping("/permit/signup")
	@PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_HUB_MANAGER')")
	public ResponseEntity<ApiResponse<Void>> permitSignup(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			@RequestBody @Valid PermitRequestDto requestDto) {
		authService.permitSignup(userDetails.getUserId(), requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_PERMIT_SUCCESS);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> login(
			@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse res) {
		authService.login(requestDto, res);
		return ResponseUtil.successResponse(SuccessCode.USER_LOGIN_SUCCESS);
	}
}
