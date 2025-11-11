package com.fastline.vendorservice.domain.repository;

import com.fastline.vendorservice.domain.entity.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

	Order insert(Order order);

	Order findByOrderIdWithProducts(UUID orderId);

	Order findByOrderId(UUID orderId);

	UUID deleteByOrderId(UUID orderId);

	List<Order> findAllByVendorId(UUID vendorId, Pageable pageable);
}
