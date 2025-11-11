package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import java.time.Instant;
import java.util.UUID;

public record DeliverySummaryResponse(
		UUID deliveryId,
		UUID orderId,
		DeliveryStatus status,
		String address,
		String recipientUsername,
		Long vendorDeliveryManagerId,
		Instant createdAt) {
	public static DeliverySummaryResponse from(Delivery delivery) {
		return new DeliverySummaryResponse(
				delivery.getDeliveryId(),
				delivery.getOrderId(),
				delivery.getStatus(),
				delivery.getAddress(),
				delivery.getRecipientUsername(),
				delivery.getVendorDeliveryManagerId(),
				delivery.getCreatedAt());
	}
}
