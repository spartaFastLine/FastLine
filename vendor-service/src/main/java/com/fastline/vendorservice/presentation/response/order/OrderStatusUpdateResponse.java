package com.fastline.vendorservice.presentation.response.order;

import com.fastline.vendorservice.domain.vo.OrderStatus;
import java.util.UUID;

public record OrderStatusUpdateResponse(UUID orderId, OrderStatus orderStatus) {}
