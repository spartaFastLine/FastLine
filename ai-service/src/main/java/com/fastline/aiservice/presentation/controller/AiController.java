package com.fastline.aiservice.presentation.controller;

import com.fastline.aiservice.application.command.GenerateMessageCommand;
import com.fastline.aiservice.application.dto.MessageGenerationResult;
import com.fastline.aiservice.application.service.RequestLogApplicationService;
import com.fastline.aiservice.presentation.dto.MessageGenerationRequest;
import com.fastline.aiservice.presentation.dto.MessageGenerationResponse;
import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

	private final RequestLogApplicationService requestLogApplicationService;

	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<MessageGenerationResponse>> generateMessage(
			@RequestBody MessageGenerationRequest request) {
		log.info("AI 메세지 생성 요청: orderId={}", request.orderId());

		// 1. Command 객체 생성
		GenerateMessageCommand command = GenerateMessageCommand.from(request);

		// 2. 애플리케이션 서비스 호출
		MessageGenerationResult result = requestLogApplicationService.generateMessage(command);

		// 3. 응용 계층 DTO -> 프레젠테이션 계층 DTO
		MessageGenerationResponse response = MessageGenerationResponse.from(result);

		log.info("AI 메세지 생성 성공");
		return ResponseUtil.successResponse(SuccessCode.MESSAGE_GENERATION_SUCCESS, response);
	}
}
