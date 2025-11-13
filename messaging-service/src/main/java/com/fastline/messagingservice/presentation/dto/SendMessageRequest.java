package com.fastline.messagingservice.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SendMessageRequest(
		UUID orderId,
		Long deliveryManagerId,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		List<Item> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination) {

	public record Item(String name, int quantity) {}
}
