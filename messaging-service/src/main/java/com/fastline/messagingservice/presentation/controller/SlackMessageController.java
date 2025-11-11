package com.fastline.messagingservice.presentation.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.messagingservice.application.command.SendMessageCommand;
import com.fastline.messagingservice.application.service.SlackMessageApplicationService;
import com.fastline.messagingservice.presentation.dto.SendMessageRequest;
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
@RequestMapping("/api/slack")
public class SlackMessageController {

	private final SlackMessageApplicationService slackMessageService;

	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<Void>> generateMessage(
			@RequestBody SendMessageRequest request) {
		log.info("Slack 메세지 전송 요청: orderId={}", request.orderId());

		SendMessageCommand command = SendMessageCommand.from(request);

		slackMessageService.send(command);

		log.info("Slack 메세지 전송 성공");

		return ResponseUtil.successResponse(SuccessCode.SLACK_MESSAGE_SENT_SUCCESS);
	}
}
