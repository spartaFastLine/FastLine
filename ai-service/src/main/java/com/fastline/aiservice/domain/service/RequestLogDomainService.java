package com.fastline.aiservice.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLogDomainService {

	private final ObjectMapper objectMapper;

	public boolean isValidJson(String input) {
		try {
			objectMapper.readTree(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String sanitizeLLM(String input) {
		if (input == null) return "";
		return input.replace("```json", "").replace("```", "").trim();
	}
}
