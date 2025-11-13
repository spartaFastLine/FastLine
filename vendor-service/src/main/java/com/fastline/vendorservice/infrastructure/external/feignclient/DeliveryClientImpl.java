package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.application.service.DeliveryClient;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryClientImpl implements DeliveryClient {

	private final DeliveryFeignClient deliveryFeignClient;

	@Override
	public DeliveryResponseDto requestDelivery(DeliveryRequestDto requestDto) {
		return deliveryFeignClient.requestDelivery(requestDto);
	}
}
