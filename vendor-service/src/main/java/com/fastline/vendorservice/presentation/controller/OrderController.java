package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.OrderService;
import com.fastline.vendorservice.application.command.CreateOrderCommand;
import com.fastline.vendorservice.application.command.CreateOrderProductCommand;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import com.fastline.vendorservice.presentation.response.order.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService service;

	@PostMapping
	public ResponseEntity<ApiResponse<OrderCreateResponse>> insertOrder(
			@RequestBody @Valid OrderCreateRequest createRequest) {

		CreateOrderCommand createCommand =
				new CreateOrderCommand(
						createRequest.vendorProducerId(),
						createRequest.vendorConsumerId(),
						createRequest.request(),
						createRequest.orderProductRequests().stream()
								.map(
										request ->
												new CreateOrderProductCommand(request.productId(), request.quantity()))
								.toList());

		Order order = service.insert(createCommand);
		List<OrderItemCreateResponse> orderItemCreateResponses =
				order.getOrderProducts().stream()
						.map(op -> new OrderItemCreateResponse(op.getProduct().getId(), op.getQuantity()))
						.toList();

		OrderCreateResponse response =
				new OrderCreateResponse(
						order.getId(),
						order.getVendorProducerId(),
						order.getVendorConsumerId(),
						order.getStatus(),
						order.getRequest(),
						orderItemCreateResponses,
						order.getDeliveryId());

		return ResponseUtil.successResponse(SuccessCode.ORDER_SAVE_SUCCESS, response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<OrderFindResponse>> findOrder(@RequestParam UUID orderId) {

		Order order = service.findByOrderId(orderId);
		List<OrderItemFindResponse> orderItemFindResponses =
				order.getOrderProducts().stream()
						.map(op -> new OrderItemFindResponse(op.getProduct().getId(), op.getQuantity()))
						.toList();

		OrderFindResponse response =
				new OrderFindResponse(
						order.getId(),
						order.getVendorProducerId(),
						order.getVendorConsumerId(),
						order.getStatus(),
						order.getRequest(),
						orderItemFindResponses,
						order.getDeliveryId());

		return ResponseUtil.successResponse(SuccessCode.ORDER_SAVE_SUCCESS, response);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<OrderStatusUpdateResponse>> updateStatus(
			@RequestParam UUID orderId, @RequestParam String status) {
		Order order = service.updateStatus(orderId, status);
		OrderStatusUpdateResponse response =
				new OrderStatusUpdateResponse(order.getId(), order.getStatus());

		return ResponseUtil.successResponse(SuccessCode.ORDER_STATUS_UPDATE_SUCCESS, response);
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<UUID>> deletedOrder(@RequestParam UUID orderId) {

		UUID deletedId = service.deleteOrder(orderId);

		return ResponseUtil.successResponse(SuccessCode.ORDER_DELETE_SUCCESS, deletedId);
	}
}
