package com.fastline.authservice.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;


public record UpdateSlackRequest(@NotBlank String slackId) {
}
