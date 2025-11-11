package com.fastline.vendorservice.infrastructure.external;

import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import com.fastline.vendorservice.infrastructure.external.dto.message.MessageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "messaging-service")
public interface MessageClient {

	@PostMapping("/api/slack/messages")
	MessageResponseDto sendMassage(MessageRequestDto messageRequestDto);
}
