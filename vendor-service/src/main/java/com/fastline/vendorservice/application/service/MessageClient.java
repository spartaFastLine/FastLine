package com.fastline.vendorservice.application.service;

import com.fastline.vendorservice.infrastructure.external.dto.message.MessageRequestDto;

public interface MessageClient {

    void sendMessage(MessageRequestDto requestDto);
}
