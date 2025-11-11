package com.fastline.messagingservice.application.command;

import com.fastline.messagingservice.presentation.dto.SendMessageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SendMessageCommand(
		UUID orderId,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		List<SendMessageRequest.Item> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination,
		String deliveryManagerName,
		String deliveryManagerEmail) {

	public static SendMessageCommand from(SendMessageRequest dto) {
		return new SendMessageCommand(
				dto.orderId(),
				dto.customerName(),
				dto.customerEmail(),
				dto.orderDateTime(),
				dto.items(),
				dto.requestNote(),
				dto.sourceHub(),
				dto.viaHubs(),
				dto.destination(),
				dto.deliveryManagerName(),
				dto.deliveryManagerEmail());
	}
}
