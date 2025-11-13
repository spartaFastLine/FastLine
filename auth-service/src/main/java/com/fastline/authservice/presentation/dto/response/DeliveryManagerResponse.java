package com.fastline.authservice.presentation.dto.response;

import java.util.UUID;

public record DeliveryManagerResponse(
		Long userId,
		String username,
		String slackId,
		UUID hubId,
		String deliveryType,
		Long deliveryNumber) {}
