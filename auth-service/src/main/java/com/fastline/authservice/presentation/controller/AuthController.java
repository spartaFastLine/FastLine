package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.application.change.DeliveryManagerService;
import com.fastline.authservice.presentation.dto.request.LoginRequest;
import com.fastline.authservice.presentation.dto.request.SignupRequest;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final DeliveryManagerService.AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid SignupRequest requestDto) {
		authService.signup(requestDto);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_SUCCESS);
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> login(
			@RequestBody @Valid LoginRequest requestDto, HttpServletResponse res) {
		authService.login(requestDto, res);
		return ResponseUtil.successResponse(SuccessCode.USER_LOGIN_SUCCESS);
	}
}
