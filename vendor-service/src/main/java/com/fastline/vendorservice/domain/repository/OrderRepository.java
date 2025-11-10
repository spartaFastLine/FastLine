package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Order;

public interface OrderRepository {

	Order insert(Order order);
}
