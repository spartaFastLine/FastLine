package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.application.dto.DeliveryPathDetailResult;
import com.fastline.deliveryservice.domain.entity.DeliveryPathStatus;
import java.util.UUID;

public record DeliveryPathDetailResponse(
		UUID pathId,
		int sequence,
		UUID fromHubId,
		UUID toHubId,
		int expDistance,
		int expDuration,
		Integer actDistance,
		Integer actDuration,
		DeliveryPathStatus status,
		Long deliveryManagerId) {
	public static DeliveryPathDetailResponse from(DeliveryPathDetailResult result) {
		return new DeliveryPathDetailResponse(
				result.pathId(),
				result.sequence(),
				result.fromHubId(),
				result.toHubId(),
				result.expDistance(),
				result.expDuration(),
				result.actDistance(),
				result.actDuration(),
				result.status(),
				result.deliveryManagerId());
	}
}
