package com.fastline.messagingservice.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SendMessageRequest(
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
}
