package com.fastline.deliveryservice.infrastructure.external.dto;

import java.time.Instant;

public record OrderDeliveryCompleteRequest(Instant arrivedTime) {}
