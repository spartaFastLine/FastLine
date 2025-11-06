package com.fastline.aiservice.controller;

import com.fastline.aiservice.dto.MessageGenerationRequest;
import com.fastline.aiservice.dto.MessageGenerationResponse;
import com.fastline.aiservice.service.AiService;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "AI")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

	private final AiService aiService;

	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<MessageGenerationResponse>> generateMessage(
			@RequestBody MessageGenerationRequest request) {
		return ResponseUtil.successResponse(
				SuccessCode.MESSAGE_GENERATION_SUCCESS, aiService.generateMessage(request));
	}
}
