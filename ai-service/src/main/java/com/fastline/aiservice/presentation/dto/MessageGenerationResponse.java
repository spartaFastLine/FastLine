package com.fastline.aiservice.presentation.dto;

import com.fastline.aiservice.application.dto.MessageGenerationResult;

public record MessageGenerationResponse(String finalDispatchDeadline) {

	public static MessageGenerationResponse from(MessageGenerationResult result) {
		return new MessageGenerationResponse(result.finalDispatchDeadline());
	}
}
