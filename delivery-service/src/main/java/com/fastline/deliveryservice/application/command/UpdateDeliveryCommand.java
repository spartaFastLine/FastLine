package com.fastline.deliveryservice.application.command;

import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import java.util.List;
import java.util.UUID;

public record UpdateDeliveryCommand(
		UUID deliveryId,
		DeliveryStatus status,
		String address,
		String recipientUsername,
		String recipientSlackId,
		Long vendorDeliveryManagerId,
		List<UpdateDeliveryPathCommand> paths) {}
