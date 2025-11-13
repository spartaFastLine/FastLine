package com.fastline.vendorservice.presentation.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.security.model.UserDetailsImpl;
import com.fastline.common.success.SuccessCode;
import com.fastline.vendorservice.application.OrderService;
import com.fastline.vendorservice.domain.model.Order;
import com.fastline.vendorservice.infrastructure.external.dto.OrderCompleteDto;
import com.fastline.vendorservice.infrastructure.swagger.OrderControllerSwagger;
import com.fastline.vendorservice.presentation.request.OrderCreateRequest;
import com.fastline.vendorservice.presentation.response.order.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController implements OrderControllerSwagger {

	private final OrderService service;

	@PostMapping
	@PreAuthorize("hasAnyRole('MASTER', 'VENDOR_MANAGER')")
	public ResponseEntity<ApiResponse<OrderCreateResponse>> insertOrder(
			@RequestBody @Valid OrderCreateRequest createRequest,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Order order = service.insert(createRequest, userDetails.getUserId());
		List<OrderItemCreateResponse> orderItemCreateResponses =
				order.getOrderProducts().stream()
						.map(op -> new OrderItemCreateResponse(op.getProduct().getId(), op.getQuantity()))
						.toList();

		OrderCreateResponse response =
				new OrderCreateResponse(
						order.getId(),
						order.getVendorProducerId(),
						order.getVendorConsumerId(),
						order.getConsumerName(),
						order.getStatus(),
						order.getRequest(),
						orderItemCreateResponses,
						order.getDeliveryId());

		return ResponseUtil.successResponse(SuccessCode.ORDER_SAVE_SUCCESS, response);
	}

	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<OrderFindResponse>> findOrder(
			@RequestParam UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		Order order = service.findByOrderId(orderId, userDetails.getUserId());
		List<OrderItemFindResponse> orderItemFindResponses =
				order.getOrderProducts().stream()
						.map(op -> new OrderItemFindResponse(op.getProduct().getId(), op.getQuantity()))
						.toList();

		OrderFindResponse response =
				new OrderFindResponse(
						order.getId(),
						order.getVendorProducerId(),
						order.getVendorConsumerId(),
						order.getConsumerName(),
						order.getStatus(),
						order.getRequest(),
						orderItemFindResponses,
						order.getDeliveryId());

		return ResponseUtil.successResponse(SuccessCode.ORDER_FIND_SUCCESS, response);
	}

	@PutMapping
	@PreAuthorize("hasAnyRole('MASTER', 'VENDOR_MANAGER')")
	public ResponseEntity<ApiResponse<OrderStatusUpdateResponse>> updateStatus(
			@RequestParam UUID orderId,
			@RequestParam String status,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		Order order = service.updateStatus(orderId, status, userDetails.getUserId());
		OrderStatusUpdateResponse response =
				new OrderStatusUpdateResponse(order.getId(), order.getStatus());

		return ResponseUtil.successResponse(SuccessCode.ORDER_STATUS_UPDATE_SUCCESS, response);
	}

	@DeleteMapping
	@PreAuthorize("hasAnyRole('MASTER', 'HUB_MANAGER')")
	public ResponseEntity<ApiResponse<UUID>> deletedOrder(
			@RequestParam UUID orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		UUID deletedId = service.deleteOrder(orderId, userDetails.getUserId());

		return ResponseUtil.successResponse(SuccessCode.ORDER_DELETE_SUCCESS, deletedId);
	}

	@PatchMapping("/{orderId}/delivery-complete")
	public ResponseEntity<ApiResponse<UUID>> deliveryCompleteNotification(
			@PathVariable UUID orderId, @RequestBody @Valid OrderCompleteDto completeDto) {

		UUID response = service.deliveryCompleteNotification(orderId, completeDto);

		return ResponseUtil.successResponse(SuccessCode.ORDER_COMPLETE, response);
	}
}
