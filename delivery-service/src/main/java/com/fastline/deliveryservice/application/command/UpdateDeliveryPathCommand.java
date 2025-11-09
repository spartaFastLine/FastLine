package com.fastline.deliveryservice.application.command;

import com.fastline.deliveryservice.domain.entity.DeliveryPathStatus;

public record UpdateDeliveryPathCommand(
		int sequence,
		Integer actDistance,
		Integer actDuration,
		DeliveryPathStatus status,
		Long deliveryManagerId) {}
