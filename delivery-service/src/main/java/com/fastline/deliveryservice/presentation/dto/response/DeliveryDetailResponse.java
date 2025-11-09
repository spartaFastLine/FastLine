package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.application.dto.DeliveryResult;
import java.util.List;
import java.util.UUID;

public record DeliveryDetailResponse(
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
		List<DeliveryPathResponse> paths) {
	public static DeliveryDetailResponse from(DeliveryResult result) {
		return new DeliveryDetailResponse(
				result.deliveryId(),
				result.orderId(),
				result.status(),
				result.vendorSenderId(),
				result.vendorReceiverId(),
				result.startHubId(),
				result.endHubId(),
				result.address(),
				result.recipientUsername(),
				result.recipientSlackId(),
				result.vendorDeliveryManagerId(),
				result.paths().stream().map(DeliveryPathResponse::from).toList());
	}
}
