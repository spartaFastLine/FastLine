package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "messaging-service")
public interface MessageFeignClient {

	@PostMapping("/api/slack/messages")
	void sendMessage(MessageRequestDto requestDto);
}
