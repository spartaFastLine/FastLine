package com.fastline.messagingservice.external.dto;

import com.fastline.messagingservice.dto.SendMessageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageGenerationRequest(
		UUID orderId,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		List<Item> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination,
		String deliveryManagerName,
		String deliveryManagerEmail) {

	public record Item(String name, int quantity) {}

	public static MessageGenerationRequest from(SendMessageRequest request) {
		return new MessageGenerationRequest(
				request.orderId(),
				request.customerName(),
				request.customerEmail(),
				request.orderDateTime(),
				request.items(),
				request.requestNote(),
				request.sourceHub(),
				request.viaHubs(),
				request.destination(),
				request.deliveryManagerName(),
				request.deliveryManagerEmail());
	}
}
