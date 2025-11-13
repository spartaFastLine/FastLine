package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.infrastructure.dto.DeliveryResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Delivery service Feign client (registered in Eureka as delivery-service) */
@FeignClient(name = "delivery-service")
public interface DelivaryClient {

	@GetMapping("/api/v1/deliveries/{deliveryId}")
	ResponseEntity<DeliveryResponseDto> getDelivery(@PathVariable UUID deliveryId);
}
