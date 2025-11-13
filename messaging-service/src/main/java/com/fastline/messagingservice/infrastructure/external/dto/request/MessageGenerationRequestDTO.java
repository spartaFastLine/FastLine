package com.fastline.messagingservice.infrastructure.external.dto.request;

import com.fastline.messagingservice.application.model.SendMessageContext;
import com.fastline.messagingservice.presentation.dto.SendMessageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageGenerationRequestDTO(
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

	public static MessageGenerationRequestDTO from(SendMessageContext context) {
		return new MessageGenerationRequestDTO(
				context.orderId(),
				context.customerName(),
				context.customerEmail(),
				context.orderDateTime(),
				context.items(),
				context.requestNote(),
				context.sourceHub(),
				context.viaHubs(),
				context.destination(),
				context.deliveryManagerName(),
				context.deliveryManagerEmail());
	}
}
