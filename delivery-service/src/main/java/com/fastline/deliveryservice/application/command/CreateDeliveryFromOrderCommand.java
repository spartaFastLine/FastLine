package com.fastline.deliveryservice.application.command;

import java.util.UUID;

public record CreateDeliveryFromOrderCommand(
		UUID orderId,
		UUID vendorSenderId,
		UUID vendorReceiverId,
		String recipientUsername,
		String recipientSlackId,
		String address) {}
