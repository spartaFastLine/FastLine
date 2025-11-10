package com.fastline.aiservice.application.command;

import com.fastline.aiservice.presentation.dto.MessageGenerationRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GenerateMessageCommand(
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

	public static GenerateMessageCommand from(MessageGenerationRequest dto) {
		return new GenerateMessageCommand(
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

	public record Item(String name, int quantity) {}
}
