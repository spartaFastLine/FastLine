package com.fastline.vendorservice.infrastructure.external.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageRequestDto(
		UUID orderId,
		Long deliveryManagerId,
		String customerName,
		String customerEmail,
		Instant orderDateTime,
		List<MessageItem> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination) {
	public record MessageItem(String name, Integer quantity) {}
}
