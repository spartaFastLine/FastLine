package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateSlackRequestDto {
	@NotBlank private String slackId;
}
