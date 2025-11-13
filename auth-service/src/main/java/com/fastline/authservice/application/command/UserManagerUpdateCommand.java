package com.fastline.authservice.application.command;

import java.util.UUID;

public record UserManagerUpdateCommand(UUID hubId, String status, String deliveryType) {}
