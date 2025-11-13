package com.fastline.authservice.application.result;

import com.fastline.authservice.domain.model.DeliveryManager;
import java.util.UUID;

public record DeliveryManagerResult(
		Long userId,
		String username,
		String slackId,
		UUID hubId,
		String deliveryType,
		Long deliveryNumber) {
	public DeliveryManagerResult(DeliveryManager deliveryManager) {
		this(
				deliveryManager.getId(),
				deliveryManager.getUser().getUsername(),
				deliveryManager.getUser().getSlackId(),
				deliveryManager.getUser().getHubId(),
				deliveryManager.getType().name(),
				deliveryManager.getNumber());
	}
}
