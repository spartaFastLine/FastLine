package com.fastline.messagingservice.application.model;

import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.application.dto.AuthResult;
import com.fastline.messagingservice.presentation.dto.SendMessageRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SendMessageContext(
		UUID orderId,
		Long deliveryManagerId,
		String deliveryManagerName,
		String deliveryManagerEmail,
		String customerName,
		String customerEmail,
		LocalDateTime orderDateTime,
		List<SendMessageRequest.Item> items,
		String requestNote,
		String sourceHub,
		List<String> viaHubs,
		String destination) {
	public static SendMessageContext of(SendMessageCommand cmd, AuthResult auth) {
		return new SendMessageContext(
				cmd.orderId(),
				cmd.deliveryManagerId(),
				auth.name(),
				auth.email(),
				cmd.customerName(),
				cmd.customerEmail(),
				cmd.orderDateTime(),
				cmd.items(),
				cmd.requestNote(),
				cmd.sourceHub(),
				cmd.viaHubs(),
				cmd.destination());
	}
}
