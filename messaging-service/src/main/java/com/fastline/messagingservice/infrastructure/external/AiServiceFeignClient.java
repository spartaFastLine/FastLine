package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.infrastructure.external.dto.request.MessageGenerationRequestDTO;
import com.fastline.messagingservice.infrastructure.external.dto.response.MessageGenerationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AiServiceFeignClient {

	@PostMapping("/api/ai/messages")
	MessageGenerationResponseDTO generate(@RequestBody MessageGenerationRequestDTO request);
}
