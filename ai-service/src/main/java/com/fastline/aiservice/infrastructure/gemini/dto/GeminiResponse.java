package com.fastline.aiservice.infrastructure.gemini.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponse(List<Candidate> candidates) {
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Candidate(Content content) {
		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Content(String role, List<Part> parts) {}

		@JsonIgnoreProperties(ignoreUnknown = true)
		public record Part(String text) {}
	}
}
