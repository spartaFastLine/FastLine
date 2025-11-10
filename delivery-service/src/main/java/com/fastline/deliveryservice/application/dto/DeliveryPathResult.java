package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import java.util.UUID;

public record DeliveryPathResult(
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		int expDistance,
		int expDuration,
		Long deliveryManagerId) {
	public static DeliveryPathResult from(DeliveryPath path) {
		return new DeliveryPathResult(
				path.getSequence(),
				path.getFromHubId(),
				path.getToHubId(),
				path.getExpDistance(),
				path.getExpDuration(),
				path.getDeliveryManagerId());
	}
}
