package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.application.service.AiClient;
import com.fastline.messagingservice.infrastructure.external.dto.MessageGenerationRequest;
import com.fastline.messagingservice.infrastructure.external.dto.MessageGenerationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiClientImpl implements AiClient {

	private final AiServiceFeignClient feignClient;

	@Override
	public String generate(SendMessageCommand command) {
		MessageGenerationRequest request = MessageGenerationRequest.from(command);
		MessageGenerationResponse response = feignClient.generate(request);

		return response.data().finalDispatchDeadline();
	}
}
