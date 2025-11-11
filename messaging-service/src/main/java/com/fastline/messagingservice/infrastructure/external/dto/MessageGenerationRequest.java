package com.fastline.messagingservice.infrastructure.external.dto;

import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.presentation.dto.SendMessageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageGenerationRequest(
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

	public static MessageGenerationRequest from(SendMessageCommand command) {
		return new MessageGenerationRequest(
				command.orderId(),
				command.customerName(),
				command.customerEmail(),
				command.orderDateTime(),
				command.items(),
				command.requestNote(),
				command.sourceHub(),
				command.viaHubs(),
				command.destination(),
				command.deliveryManagerName(),
				command.deliveryManagerEmail());
	}
}
