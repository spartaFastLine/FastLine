package com.fastline.authservice.application.command;

import jakarta.validation.constraints.NotBlank;

public record LoginCommand(@NotBlank String username, @NotBlank String password) {}
