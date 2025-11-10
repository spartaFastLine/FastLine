package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.OrderService;
import com.fastline.vendorservice.application.command.CreateOrderCommand;
import com.fastline.vendorservice.application.command.CreateOrderProductCommand;
import com.fastline.vendorservice.domain.entity.Order;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import com.fastline.vendorservice.presentation.response.order.OrderCreateResponse;
import com.fastline.vendorservice.presentation.response.order.OrderItemCreateResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
