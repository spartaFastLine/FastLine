package com.fastline.vendorservice.presentation.response.order;

import java.util.UUID;

public record OrderItemCreateResponse(UUID productId, Integer quantity) {}
