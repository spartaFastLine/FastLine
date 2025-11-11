package com.fastline.vendorservice.domain.service;

import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorOrderService {

	private final OrderRepository orderRepository;

	public List<Order> findOrdersInVendor(UUID vendorId, Pageable pageable) {
		return orderRepository.findAllByVendorId(vendorId, pageable);
	}
}
