package com.fastline.deliveryservice.application.dto;

import com.fastline.deliveryservice.domain.entity.DeliveryPath;
import com.fastline.deliveryservice.domain.entity.DeliveryPathStatus;

import java.util.UUID;

public record DeliveryPathDetailResult(
        UUID pathId,
        int sequence,
        UUID fromHubId,
        UUID toHubId,
        int expDistance,
        int expDuration,
        Integer actDistance,
        Integer actDuration,
        DeliveryPathStatus status,
        Long deliveryManagerId
) {
    public static DeliveryPathDetailResult from(DeliveryPath path) {
        return new DeliveryPathDetailResult(
                path.getDeliveryPathId(),
                path.getSequence(),
                path.getFromHubId(),
                path.getToHubId(),
                path.getExpDistance(),
                path.getExpDuration(),
                path.getActDistance(),
                path.getActDuration(),
                path.getStatus(),
                path.getDeliveryManagerId()
        );
    }
}