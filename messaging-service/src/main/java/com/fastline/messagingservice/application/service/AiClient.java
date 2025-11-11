package com.fastline.messagingservice.application.service;

import com.fastline.messagingservice.application.command.SendMessageCommand;

public interface AiClient {
	String generate(SendMessageCommand command);
}
