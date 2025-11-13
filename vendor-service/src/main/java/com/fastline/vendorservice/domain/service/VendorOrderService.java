package com.fastline.vendorservice.domain.service;

import com.fastline.vendorservice.domain.model.Order;
import com.fastline.vendorservice.domain.model.Vendor;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VendorOrderService {

	private final OrderRepository orderRepository;
	private final VendorRepository vendorRepository;

	public List<Order> findOrdersInVendor(UUID vendorId, Pageable pageable) {
		return orderRepository.findAllByVendorId(vendorId, pageable);
	}

	public boolean isVendorOrder(Order order, Long userId) {

		List<Vendor> vendors =
				vendorRepository.findVendorByUserIdAndConsumerId(userId, order.getVendorConsumerId());

		return vendors.stream().anyMatch(v -> order.getVendorConsumerId().equals(v.getId()));
	}

	public boolean isHubOrder(Order order, UUID hubId) {
		List<Vendor> vendorsInHub = vendorRepository.findAllByHubId(hubId);
		return vendorsInHub.stream()
				.anyMatch(
						v ->
								order.getVendorConsumerId().equals(v.getId())
										|| order.getVendorProducerId().equals(v.getId()));
	}
}
