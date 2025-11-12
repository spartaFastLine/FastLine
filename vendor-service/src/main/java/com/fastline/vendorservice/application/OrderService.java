package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.service.DeliveryClient;
import com.fastline.vendorservice.application.service.MessageClient;
import com.fastline.vendorservice.application.service.UserClient;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.domain.entity.OrderProduct;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.repository.VendorRepository;
import com.fastline.vendorservice.domain.service.OrderProductService;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryResponseDto;
import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;
import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository repository;
	private final OrderProductService orderProductService;

    private final DeliveryClient deliveryClient;
	private final UserClient userClient;
    private final MessageClient messageClient;

	public Order insert(OrderCreateRequest createRequest, Long userId) {

		Order order = Order.create(createRequest);
		List<OrderProduct> orderProducts =
				orderProductService.createOrderProducts(order, createRequest.orderProductRequests());
		orderProducts.forEach(order::mappingOrderProduct);

        UserResponseDto userInfo = userClient.getUserInfo(userId);

        DeliveryResponseDto deliveryResponseDto = deliveryClient.requestDelivery(new DeliveryRequestDto(
                order.getId().toString(),
                order.getVendorProducerId().toString(),
                order.getVendorConsumerId().toString(),
                order.getConsumerName(),
                userInfo.slackId(),
                createRequest.address()
        ));

        order.mappingDeliveryId(UUID.fromString(deliveryResponseDto.deliveryId()));
		Order result = repository.insert(order);

        messageClient.sendMessage(createMessageRequestDto(result, userInfo, deliveryResponseDto, createRequest.address()));

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

	private MessageRequestDto createMessageRequestDto(Order order, UserResponseDto userInfo, DeliveryResponseDto deliveryResponseDto, String destination) {

		ArrayList<MessageRequestDto.MessageItem> messageItems =
                order.getOrderProducts().stream()
						.map(
								product ->
										new MessageRequestDto.MessageItem(
												product.getProduct().getName(), product.getQuantity()))
						.collect(Collectors.toCollection(ArrayList::new));

		return new MessageRequestDto(
				order.getId(),
                deliveryResponseDto.managerId(),
				order.getConsumerName(),
                userInfo.email(),
                Instant.now(),
				messageItems,
				order.getRequest(),
				deliveryResponseDto.hubPath().get(0),
				deliveryResponseDto.hubPath().subList(1, deliveryResponseDto.hubPath().size()),
				destination);
	}
}
