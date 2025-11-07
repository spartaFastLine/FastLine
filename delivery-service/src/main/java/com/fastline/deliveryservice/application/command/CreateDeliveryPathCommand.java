package com.fastline.deliveryservice.application.command;

import java.util.UUID;

public record CreateDeliveryPathCommand(
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		int expDistance,
		int expDuration,
		Long deliveryManagerId) {}
