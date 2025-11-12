package com.fastline.deliveryservice.presentation.dto.response;

import com.fastline.deliveryservice.domain.entity.DeliveryStatus;
import java.util.UUID;

public record DeliveryStatusUpdateResponse(UUID deliveryId, DeliveryStatus status) {}
