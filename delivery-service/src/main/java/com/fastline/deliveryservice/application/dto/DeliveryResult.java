package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.domain.entity.Delivery;
import java.util.List;
import java.util.UUID;

public record DeliveryResult(
		UUID deliveryId,
		UUID orderId,
		String status,
		UUID vendorSenderId,
		UUID vendorReceiverId,
		UUID startHubId,
		UUID endHubId,
		String address,
		String recipientUsername,
		String recipientSlackId,
		Long vendorDeliveryManagerId,
		List<DeliveryPathResult> paths) {
	public static DeliveryResult from(Delivery delivery) {
		return new DeliveryResult(
				delivery.getDeliveryId(),
				delivery.getOrderId(),
				delivery.getStatus().toString(),
				delivery.getVendorSenderId(),
				delivery.getVendorReceiverId(),
				delivery.getStartHubId(),
				delivery.getEndHubId(),
				delivery.getAddress(),
				delivery.getRecipientUsername(),
				delivery.getRecipientSlackId(),
				delivery.getVendorDeliveryManagerId(),
				delivery.getPaths().stream().map(DeliveryPathResult::from).toList());
	}
}
