package com.fastline.messagingservice.infrastructure.external.dto;

public record MessageGenerationResponse(
		Boolean success, String message, Data data, String errorCode) {
	public record Data(String finalDispatchDeadline) {}
}
