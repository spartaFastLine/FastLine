package com.fastline.messagingservice.infrastructure.external.dto.response;

public record DeliveryManagerInfoResponseDTO(
		Boolean success, String message, Data data, String errorCode) {
	public record Data(String slackId, String name, String email) {}
}
