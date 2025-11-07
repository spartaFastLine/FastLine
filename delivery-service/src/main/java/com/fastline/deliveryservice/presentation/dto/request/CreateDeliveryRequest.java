package com.fastline.deliveryservice.presentation.dto.request;

import java.util.List;
import java.util.UUID;

public record CreateDeliveryRequest(
		UUID orderId,
		UUID vendorSenderId,
		UUID vendorReceiverId,
		UUID startHubId,
		UUID endHubId,
		String address,
		String recipientUsername,
		String recipientSlackId,
		Long vendorDeliveryManagerId,
		List<CreateDeliveryPathRequest> paths) {}
