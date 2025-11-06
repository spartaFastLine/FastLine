package com.fastline.aiservice.client;

import com.fastline.aiservice.dto.gemini.GeminiRequest;
import com.fastline.aiservice.dto.gemini.GeminiResponse;
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
