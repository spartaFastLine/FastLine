package com.fastline.deliveryservice.presentation;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.deliveryservice.application.DeliveryService;
import com.fastline.deliveryservice.application.command.CreateDeliveryCommand;
import com.fastline.deliveryservice.application.command.CreateDeliveryPathCommand;
import com.fastline.deliveryservice.application.dto.DeliveryCreateResponse;
import com.fastline.deliveryservice.presentation.dto.request.CreateDeliveryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliverys")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

	@PostMapping
	public ResponseEntity<ApiResponse<DeliveryCreateResponse>> createDelivery(@Valid @RequestBody CreateDeliveryRequest request) {

        CreateDeliveryCommand command = new CreateDeliveryCommand(
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
                        .map(p -> new CreateDeliveryPathCommand(
                                p.sequence(),
                                p.fromHubId(),
                                p.toHubId(),
                                p.expDistance(),
                                p.expDuration(),
                                p.deliveryManagerId()
                        ))
                        .toList()
        );

        UUID deliveryId = deliveryService.createDelivery(command);

        DeliveryCreateResponse response = new DeliveryCreateResponse(
                deliveryId
        );

        return ResponseUtil.successResponse(SuccessCode.DELIVERY_SAVE_SUCCESS, response);
	}
}
