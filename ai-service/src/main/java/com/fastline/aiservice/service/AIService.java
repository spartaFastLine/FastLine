 package com.fastline.aiservice.service;

 import com.fastline.aiservice.client.GeminiClient;
 import com.fastline.aiservice.dto.gemini.GeminiRequest;
 import com.fastline.aiservice.dto.gemini.GeminiResponse;
 import lombok.RequiredArgsConstructor;
 import org.springframework.beans.factory.annotation.Value;
 import org.springframework.stereotype.Service;

 import java.util.List;

 @Service
 @RequiredArgsConstructor
 public class AIService {

	private final GeminiClient geminiClient;

	@Value("${gemini.api-key}")
	private String apiKey;

	@Value("${gemini.model}")
	private String model;

	public String askGemini() {

		String testPrompt = "hi";

		GeminiRequest body = new GeminiRequest(
				List.of(new GeminiRequest.Content(
						List.of(new GeminiRequest.Part(testPrompt))
				))
		);

		GeminiResponse result = geminiClient.generate(body, model, apiKey);

		return result.candidates().get(0).content().parts().get(0).text();
	}
 }
