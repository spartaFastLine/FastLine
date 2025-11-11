package com.fastline.aiservice.infrastructure.gemini;

import com.fastline.aiservice.infrastructure.gemini.dto.GeminiRequest;
import com.fastline.aiservice.infrastructure.gemini.dto.GeminiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "geminiClient", url = "${gemini.base-url}")
public interface GeminiClient {

	@PostMapping("/v1beta/models/{model}:generateContent?key={apiKey}")
	GeminiResponse generate(
			@RequestBody GeminiRequest request,
			@PathVariable("model") String model,
			@PathVariable("apiKey") String apiKey);
}
