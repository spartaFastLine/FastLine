package com.fastline.vendorservice.application.service;

import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryResponseDto;

public interface DeliveryClient {

	DeliveryResponseDto requestDelivery(DeliveryRequestDto requestDto);
}
