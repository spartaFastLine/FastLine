package com.fastline.vendorservice.infrastructure.repository;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import java.util.UUID;
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

	@Override
	public Order findByOrderIdWithProducts(UUID orderId) {
		return jpaOrderRepository
				.findByOrderIdFetchJoin(orderId)
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	@Override
	public Order findByOrderId(UUID orderId) {
		return jpaOrderRepository
				.findById(orderId)
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	@Override
	public UUID deleteByOrderId(UUID orderId) {
		jpaOrderRepository.deleteById(orderId);
		return orderId;
	}
}
