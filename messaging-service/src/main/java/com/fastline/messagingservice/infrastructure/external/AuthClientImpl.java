package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.application.dto.AuthResult;
import com.fastline.messagingservice.application.service.AuthClient;
import com.fastline.messagingservice.infrastructure.external.dto.response.DeliveryManagerInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthClientImpl implements AuthClient {

	private final AuthServiceFeignClient feignClient;

	@Override
	public AuthResult loadInfo(Long deliveryManagerId) {
		DeliveryManagerInfoResponseDTO response = feignClient.loadInfo(deliveryManagerId);
		return AuthResult.of(response.data().name(), response.data().email());
	}
}
