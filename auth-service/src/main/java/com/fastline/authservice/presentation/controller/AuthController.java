package com.fastline.authservice.presentation.controller;

import com.fastline.authservice.application.command.LoginCommand;
import com.fastline.authservice.application.command.SignupCommand;
import com.fastline.authservice.application.service.AuthService;
import com.fastline.authservice.infrastructure.swagger.AuthControllerSwagger;
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
public class AuthController implements AuthControllerSwagger {
	private final AuthService authService;

	// 회원가입
	@Override
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid SignupRequest request) {
		SignupCommand command =
				new SignupCommand(
						request.username(),
						request.password(),
						request.email(),
						request.slackId(),
						request.roll(),
						request.hubId(),
						request.deliveryType());
		authService.signup(command);
		return ResponseUtil.successResponse(SuccessCode.USER_SIGNUP_SUCCESS);
	}

	// 로그인
	@Override
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<Void>> login(
			@RequestBody @Valid LoginRequest requestDto, HttpServletResponse res) {
		LoginCommand command = new LoginCommand(requestDto.username(), requestDto.password());
		authService.login(command, res);
		return ResponseUtil.successResponse(SuccessCode.USER_LOGIN_SUCCESS);
	}
}
