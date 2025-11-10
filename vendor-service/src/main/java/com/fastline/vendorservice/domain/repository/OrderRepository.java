package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Order;
import java.util.UUID;

public interface OrderRepository {

	Order insert(Order order);

	Order findByOrderIdWithProducts(UUID orderId);

	Order findByOrderId(UUID orderId);

	UUID deleteByOrderId(UUID orderId);
}
