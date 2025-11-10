package com.fastline.vendorservice.application;

import com.fastline.vendorservice.application.command.CreateOrderCommand;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.service.OrderProductService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repository;
	private final OrderProductService orderProductService;

	//    private final DeliveryClient deliveryClient;

	/** TODO: 배송서비스에 배송 생성을 요청하는 흐름 필요. */
	public Order insert(CreateOrderCommand createCommand) {

		Order order = Order.create(createCommand);
		List<OrderProduct> orderProducts =
				orderProductService.createOrderProducts(order, createCommand.orderProductCreateRequests());
		orderProducts.forEach(order::mappingOrderProduct);

		//        UUID deliveryId = deliveryClient.
		order.mappingDeliveryId(UUID.randomUUID());
		return repository.insert(order);
	}

	@Transactional(readOnly = true)
	public Order findByOrderId(UUID orderId) {
		return repository.findByOrderId(orderId);
	}
}
