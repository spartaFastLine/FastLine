package com.fastline.messagingservice.infrastructure.external;

import com.fastline.messagingservice.infrastructure.external.dto.MessageGenerationRequest;
import com.fastline.messagingservice.infrastructure.external.dto.MessageGenerationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service")
public interface AiServiceFeignClient {

	@PostMapping("/api/ai/messages")
	MessageGenerationResponse generate(@RequestBody MessageGenerationRequest request);
}
