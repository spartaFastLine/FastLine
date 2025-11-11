package com.fastline.deliveryservice.presentation.dto.request;

import com.fastline.deliveryservice.domain.entity.DeliveryStatus;

public record UpdateDeliveryStatusRequest(
        DeliveryStatus status
) {}
