package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.application.dto.DeliveryPathResult;
import java.util.UUID;

public record DeliveryPathResponse(
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		int expDistance,
		int expDuration,
		Long deliveryManagerId) {
	public static DeliveryPathResponse from(DeliveryPathResult result) {
		return new DeliveryPathResponse(
				result.sequence(),
				result.fromHubId(),
				result.toHubId(),
				result.expDistance(),
				result.expDuration(),
				result.deliveryManagerId());
	}
}
