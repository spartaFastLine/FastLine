package com.fastline.deliveryservice.application.dto;

import java.util.List;
import java.util.UUID;

public record DeliveryFromOrderCreateResult(
        UUID deliveryId,
        Long managerId,
        List<String> routes
) {
    public static DeliveryFromOrderCreateResult of(UUID deliveryId, Long managerId, List<String> routes) {
        return new DeliveryFromOrderCreateResult(deliveryId, managerId, routes);
    }
}