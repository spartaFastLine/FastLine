package com.fastline.messagingservice.external.client;

import com.fastline.messagingservice.external.dto.MessageGenerationRequest;
import com.fastline.messagingservice.external.dto.MessageGenerationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "aiServiceClient", url = "${ai-service.url}")
public interface AiServiceClient {

	@PostMapping("/api/ai/messages")
	MessageGenerationResponse generate(@RequestBody MessageGenerationRequest request);
}
