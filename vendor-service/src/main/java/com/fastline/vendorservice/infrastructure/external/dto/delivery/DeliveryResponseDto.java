package com.fastline.vendorservice.infrastructure.external.dto.delivery;

import java.util.List;

public record DeliveryResponseDto(String deliveryId, Long managerId, List<String> hubPath) {}
