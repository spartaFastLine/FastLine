package com.fastline.vendorservice.infrastructure.external.feignclient;

import com.fastline.vendorservice.application.service.MessageClient;
import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageClientImpl implements MessageClient {

	private final MessageFeignClient messageFeignClient;

	@Override
	public void sendMessage(MessageRequestDto requestDto) {
		messageFeignClient.sendMessage(requestDto);
	}
}
