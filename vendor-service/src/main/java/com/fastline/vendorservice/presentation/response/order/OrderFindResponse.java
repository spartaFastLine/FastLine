package com.fastline.vendorservice.presentation.response.order;

import com.fastline.vendorservice.domain.vo.OrderStatus;
import java.util.List;
import java.util.UUID;

public record OrderFindResponse(
		UUID orderId,
		UUID vendorProducerId,
		UUID vendorConsumerId,
		String consumerName,
		OrderStatus status,
		String request,
		List<OrderItemFindResponse> orderItems,
		UUID deliveryId) {}
