package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.application.dto.DeliveryFromOrderCreateResult;
import java.util.List;
import java.util.UUID;

public record DeliveryFromOrderCreateResponse(
		UUID deliveryId, Long managerId, List<String> routes) {
	public static DeliveryFromOrderCreateResponse from(DeliveryFromOrderCreateResult result) {
		return new DeliveryFromOrderCreateResponse(
				result.deliveryId(), result.managerId(), result.routes());
	}
}
