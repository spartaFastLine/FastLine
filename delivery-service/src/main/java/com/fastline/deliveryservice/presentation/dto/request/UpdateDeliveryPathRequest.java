package com.fastline.deliveryservice.presentation.dto.request;

import com.fastline.deliveryservice.domain.entity.DeliveryPathStatus;

public record UpdateDeliveryPathRequest(
		int sequence,
		Integer actDistance,
		Integer actDuration,
		DeliveryPathStatus status,
		Long deliveryManagerId) {}
