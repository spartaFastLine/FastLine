package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.application.model.SendMessageContext;
import com.fastline.messagingservice.application.service.AiClient;
import com.fastline.messagingservice.infrastructure.external.dto.request.MessageGenerationRequestDTO;
import com.fastline.messagingservice.infrastructure.external.dto.response.MessageGenerationResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiClientImpl implements AiClient {

	private final AiServiceFeignClient feignClient;

	@Override
	public String generate(SendMessageContext context) {
		MessageGenerationRequestDTO request = MessageGenerationRequestDTO.from(context);
		MessageGenerationResponseDTO response = feignClient.generate(request);
		return response.data().finalDispatchDeadline();
	}
}
