package com.fastline.vendorservice.application.command;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(
		UUID vendorProducerId,
		UUID vendorConsumerId,
		String consumerName,
		String request,
		List<CreateOrderProductCommand> orderProductCreateRequests) {}
