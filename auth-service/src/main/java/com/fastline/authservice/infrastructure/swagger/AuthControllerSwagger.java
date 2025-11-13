package com.fastline.authservice.infrastructure.swagger;

import com.fastline.authservice.presentation.dto.request.LoginRequest;
import com.fastline.authservice.presentation.dto.request.SignupRequest;
import com.fastline.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증 API", description = "회원가입 및 로그인 API")
public interface AuthControllerSwagger {

	@Operation(summary = "회원가입", description = "사용자 회원가입을 처리합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "201",
				description = "회원가입 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "400",
				description = "잘못된 요청"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "409",
				description = "이미 존재하는 사용자")
	})
	@PostMapping("/signup")
	ResponseEntity<ApiResponse<Void>> signup(@RequestBody @Valid SignupRequest request);

	@Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
	@ApiResponses({
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "200",
				description = "로그인 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "401",
				description = "인증 실패"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
				responseCode = "403",
				description = "권한 없음")
	})
	@PostMapping("/login")
	ResponseEntity<ApiResponse<Void>> login(
			@RequestBody @Valid LoginRequest requestDto, HttpServletResponse res);
}
