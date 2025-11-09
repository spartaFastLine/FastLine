package com.fastline.deliveryservice.presentation;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.deliveryservice.application.DeliveryService;
import com.fastline.deliveryservice.application.command.CreateDeliveryCommand;
import com.fastline.deliveryservice.application.command.CreateDeliveryPathCommand;
import com.fastline.deliveryservice.presentation.dto.request.CreateDeliveryRequest;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryCreateResponse;
import com.fastline.deliveryservice.presentation.dto.response.DeliveryDetailResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/deliverys")
@RequiredArgsConstructor
public class DeliveryController {

	private final DeliveryService deliveryService;

	/* 배송 등록 */
	@PostMapping
	public ResponseEntity<ApiResponse<DeliveryCreateResponse>> createDelivery(
			@Valid @RequestBody CreateDeliveryRequest request) {
		log.info("배송 등록 요청: orderId={}", request.orderId());

		CreateDeliveryCommand command =
				new CreateDeliveryCommand(
						request.orderId(),
						request.vendorSenderId(),
						request.vendorReceiverId(),
						request.startHubId(),
						request.endHubId(),
						request.address(),
						request.recipientUsername(),
						request.recipientSlackId(),
						request.vendorDeliveryManagerId(),
						request.paths().stream()
								.map(
										p ->
												new CreateDeliveryPathCommand(
														p.sequence(),
														p.fromHubId(),
														p.toHubId(),
														p.expDistance(),
														p.expDuration(),
														p.deliveryManagerId()))
								.toList());

		UUID deliveryId = deliveryService.createDelivery(command);

		DeliveryCreateResponse response = new DeliveryCreateResponse(deliveryId);

		return ResponseUtil.successResponse(SuccessCode.DELIVERY_SAVE_SUCCESS, response);
	}

	/* 배송 조회 */
	@GetMapping("/{deliveryId}")
	public ResponseEntity<ApiResponse<DeliveryDetailResponse>> getDelivery(
			@PathVariable UUID deliveryId) {
		log.info("배송 조회 요청: deliveryId={}", deliveryId);

		DeliveryDetailResponse response =
				DeliveryDetailResponse.from(deliveryService.getDelivery(deliveryId));

		log.info("배송 조회 성공: deliveryId={}", deliveryId);
		return ResponseUtil.successResponse(SuccessCode.DELIVERY_FIND_SUCCESS, response);
	}
}
