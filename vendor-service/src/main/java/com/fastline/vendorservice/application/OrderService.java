package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.service.OrderProductService;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import com.fastline.vendorservice.infrastructure.external.MessageClient;
import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
	private final MessageClient messageClient;

	/** TODO: 배송서비스에 배송 생성을 요청하는 흐름 필요. */
	public Order insert(OrderCreateRequest createRequest) {

		Order order = Order.create(createRequest);
		List<OrderProduct> orderProducts =
				orderProductService.createOrderProducts(order, createRequest.orderProductRequests());
		orderProducts.forEach(order::mappingOrderProduct);

		//        UUID deliveryId = deliveryClient. 받아와서 다시 해당 배송정보를 요청
		order.mappingDeliveryId(UUID.randomUUID());
		Order result = repository.insert(order);

		//		messageClient.sendMassage(createMessageRequestDto(result));

		return result;
	}

	@Transactional(readOnly = true)
	public Order findByOrderId(UUID orderId) {
		return repository.findByOrderIdWithProducts(orderId);
	}

	public Order updateStatus(UUID orderId, String status) {

		OrderStatus newStatus = OrderStatus.fromString(status);
		Order order = repository.findByOrderId(orderId);

		if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
			throw new CustomException(ErrorCode.ORDER_STATUS_UPDATE_FAIL);
		}

		order.updateStatus(newStatus);
		orderProductService.adjustQuantity(order, order.getOrderProducts());

		return order;
	}

	public UUID deleteOrder(UUID orderId) {

		return repository.deleteByOrderId(orderId);
	}

	private MessageRequestDto createMessageRequestDto(Order order) {

		List<OrderProduct> orderProducts = order.getOrderProducts();
		ArrayList<MessageRequestDto.MessageItem> messageItems =
				orderProducts.stream()
						.map(
								product ->
										new MessageRequestDto.MessageItem(
												product.getProduct().getName(), product.getQuantity()))
						.collect(Collectors.toCollection(ArrayList::new));

		return new MessageRequestDto(
				order.getId(),
				order.getConsumerName(),
				"test123@gmail.com",
				order.getCreatedAt(),
				messageItems,
				order.getRequest(),
				"testHub",
				List.of("hub1, hub2"),
				"도착지",
				"매니저",
				"manager@gmail.com");
	}
}
