package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.delivery.DeliveryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "delivery-service")
public interface DeliveryFeignClient {

    @PostMapping("/api/deliveries/from-order")
    DeliveryResponseDto requestDelivery(DeliveryRequestDto requestDto);
}
