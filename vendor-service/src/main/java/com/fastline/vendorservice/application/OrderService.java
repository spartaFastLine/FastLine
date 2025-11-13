package com.fastline.vendorservice.application;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.vendorservice.application.service.DeliveryClient;
import com.fastline.vendorservice.application.service.MessageClient;
import com.fastline.vendorservice.application.service.UserClient;
import com.fastline.vendorservice.domain.model.Order;
import com.fastline.vendorservice.domain.model.OrderProduct;
import com.fastline.vendorservice.domain.repository.OrderRepository;
import com.fastline.vendorservice.domain.service.OrderProductService;
import com.fastline.vendorservice.domain.service.VendorOrderService;
import com.fastline.vendorservice.domain.vo.OrderStatus;
import com.fastline.vendorservice.infrastructure.external.dto.OrderCompleteDto;
import com.fastline.vendorservice.infrastructure.external.dto.UserResponseDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryResponseDto;
import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import java.time.Instant;
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
	private final VendorOrderService vendorOrderService;

	private final DeliveryClient deliveryClient;
	private final UserClient userClient;
	private final MessageClient messageClient;

	public Order insert(OrderCreateRequest createRequest, Long userId) {

		Order order = Order.create(createRequest);
		List<OrderProduct> orderProducts =
				orderProductService.createOrderProducts(order, createRequest.orderProductRequests());
		orderProducts.forEach(order::mappingOrderProduct);

		UserResponseDto userInfo = userClient.getUserInfo(userId);

		DeliveryResponseDto deliveryResponseDto =
				deliveryClient.requestDelivery(
						new DeliveryRequestDto(
								order.getId().toString(),
								order.getVendorProducerId().toString(),
								order.getVendorConsumerId().toString(),
								order.getConsumerName(),
								userInfo.slackId(),
								createRequest.address()));

		order.mappingDeliveryId(UUID.fromString(deliveryResponseDto.deliveryId()));
		Order result = repository.insert(order);

		messageClient.sendMessage(
				createMessageRequestDto(result, userInfo, deliveryResponseDto, createRequest.address()));

		return result;
	}

	@Transactional(readOnly = true)
	public Order findByOrderId(UUID orderId, Long userId) {

		if (userId == 1L) return repository.findByOrderIdWithProducts(orderId);

		UUID userHubId = userClient.getUserHubId(userId);
		Order order = repository.findByOrderId(orderId);

		boolean isHubOrder = vendorOrderService.isHubOrder(order, userHubId); // 허브관리자가 관리하는 업체의 주문인가
		boolean isVendorOrder = vendorOrderService.isVendorOrder(order, userId); // 벤더관리자가 관리하는 업체의 주문인가

		if (!isHubOrder && !isVendorOrder) {
			throw new CustomException(ErrorCode.ORDER_FORBIDDEN);
		}

		return repository.findByOrderIdWithProducts(orderId);
	}

	public Order updateStatus(UUID orderId, String status, Long userId) {

		Order order = repository.findByOrderId(orderId);
		if (!isMasterUser(userId)) {
			boolean isVendorOrder = vendorOrderService.isVendorOrder(order, userId);
			if (!isVendorOrder) {
				throw new CustomException(ErrorCode.ORDER_FORBIDDEN);
			}
		}

		OrderStatus newStatus = OrderStatus.fromString(status);

		if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
			throw new CustomException(ErrorCode.ORDER_STATUS_UPDATE_FAIL);
		}

		order.updateStatus(newStatus);
		orderProductService.adjustQuantity(order, order.getOrderProducts());

		return order;
	}

	public UUID deleteOrder(UUID orderId, Long userId) {

		Order order = repository.findByOrderId(orderId);

		if (isMasterUser(userId)) {
			return repository.deleteByOrderId(orderId);
		}

		UUID userHubId = userClient.getUserHubId(userId);
		boolean isHubOrder = vendorOrderService.isHubOrder(order, userHubId);

		if (!isHubOrder) {
			throw new CustomException(ErrorCode.ORDER_FORBIDDEN);
		}

		return repository.deleteByOrderId(orderId);
	}

	public UUID deliveryCompleteNotification(UUID orderId, OrderCompleteDto completeDto) {

		Order order = repository.findByOrderId(orderId);
		order.complete(completeDto.arrivedTime());
		return order.getId();
	}

	private MessageRequestDto createMessageRequestDto(
			Order order,
			UserResponseDto userInfo,
			DeliveryResponseDto deliveryResponseDto,
			String destination) {

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

	private boolean isMasterUser(Long userId) {
		return userId == 1L;
	}
}
