package com.fastline.authservice.presentation.dto.response;

import com.fastline.authservice.domain.model.DeliveryManager;
import java.util.UUID;
import lombok.Getter;

@Getter
public class DeliveryManagerResponse {
	private final Long userId;
	private final UUID hubId;
	private final String type;
	private final Long number;

	public DeliveryManagerResponse(DeliveryManager deliveryManager) {
		this.userId = deliveryManager.getId();
		this.hubId = deliveryManager.getUser().getHubId();
		this.type = deliveryManager.getType().name();
		this.number = deliveryManager.getNumber();
	}
}
