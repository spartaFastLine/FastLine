package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import java.util.UUID;

public record DeliveryPathSummaryResponse(
		UUID pathId,
		UUID deliveryId,
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		int expDistance,
		int expDuration,
		Long deliveryManagerId) {
	public static DeliveryPathSummaryResponse from(DeliveryPath path) {
		return new DeliveryPathSummaryResponse(
				path.getDeliveryPathId(),
				path.getDelivery().getDeliveryId(),
				path.getSequence(),
				path.getFromHubId(),
				path.getToHubId(),
				path.getExpDistance(),
				path.getExpDuration(),
				path.getDeliveryManagerId());
	}
}
