package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.util.UUID;

public record SignupRequest(@Pattern(
		regexp = "^[a-z0-9]{4,10}$",
		message = "username은  최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다") String username, @Pattern(
		regexp = "[A-Za-z0-9!@#$%]{8,15}$",
		message =
				"비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대문자(A~Z), 알파벳 소문자(a~z), 숫자(0~9), 특수문자(!@#$%)로 구성되어야 합니다") String password,
							@Email @NotBlank String email, @NotBlank String slackId, @NotBlank String roll,
							@NotNull UUID hubId) {
}
