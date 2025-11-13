package com.fastline.messagingservice.application.service;

import com.fastline.messagingservice.application.model.SendMessageContext;

public interface AiClient {
	String generate(SendMessageContext context);
}
