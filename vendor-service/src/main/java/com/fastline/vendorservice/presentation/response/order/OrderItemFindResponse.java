package com.fastline.vendorservice.presentation.response.order;

import java.util.UUID;

public record OrderItemFindResponse(UUID productId, Integer quantity) {}
