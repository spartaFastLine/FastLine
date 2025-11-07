package com.fastline.messagingservice.dto;

import com.fastline.messagingservice.external.dto.MessageGenerationRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SendMessageRequest(
		UUID orderId,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		List<MessageGenerationRequest.Item> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination,
		String deliveryManagerName,
		String deliveryManagerEmail) {

	public record Item(String name, int quantity) {}
}
