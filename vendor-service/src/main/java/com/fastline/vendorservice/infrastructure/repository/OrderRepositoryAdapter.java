package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

	private final JpaOrderRepository jpaOrderRepository;

	@Override
	public Order insert(Order order) {
		return jpaOrderRepository.save(order);
	}
}
