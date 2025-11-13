package com.fastline.authservice.application.command;

import jakarta.validation.constraints.NotBlank;

public record UpdateSlackCommand(@NotBlank String slackId) {}
