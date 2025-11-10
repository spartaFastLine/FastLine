package com.fastline.deliveryservice.presentation.dto.request;

import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import java.util.List;

public record UpdateDeliveryRequest(
		DeliveryStatus status,
		String address,
		String recipientUsername,
		String recipientSlackId,
		Long vendorDeliveryManagerId,
		List<UpdateDeliveryPathRequest> paths) {}
