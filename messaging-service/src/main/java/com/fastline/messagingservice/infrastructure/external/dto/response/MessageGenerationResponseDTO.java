package com.fastline.messagingservice.infrastructure.external.dto.response;

public record MessageGenerationResponseDTO(
		Boolean success, String message, Data data, String errorCode) {
	public record Data(String finalDispatchDeadline) {}
}
