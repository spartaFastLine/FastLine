package com.fastline.authservice.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordCommand(
		@NotBlank String password,
		@Pattern(
						regexp = "[A-Za-z0-9!@#$%]{8,15}$",
						message =
								"비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대문자(A~Z), 알파벳 소문자(a~z), 숫자(0~9), 특수문자(!@#$%)로 구성되어야 합니다")
				String newPassword) {}
