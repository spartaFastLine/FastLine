package com.fastline.messagingservice.controller;

import com.fastline.common.response.ApiResponse;
import com.fastline.common.response.ResponseUtil;
import com.fastline.common.success.SuccessCode;
import com.fastline.messagingservice.dto.SendMessageRequest;
import com.fastline.messagingservice.service.SlackMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/slack")
public class SlackMessageController {

	private final SlackMessageService slackMessageService;

	@PostMapping("/messages")
	public ResponseEntity<ApiResponse<Void>> generateMessage(
			@RequestBody SendMessageRequest request) {
		slackMessageService.sendMessage(request);
		return ResponseUtil.successResponse(SuccessCode.SLACK_MESSAGE_SENT_SUCCESS);
	}
}
