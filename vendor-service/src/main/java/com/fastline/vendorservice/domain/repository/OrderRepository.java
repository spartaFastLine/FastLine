package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Order;
import java.util.UUID;

public interface OrderRepository {

	Order insert(Order order);

	Order findByOrderId(UUID orderId);
}
