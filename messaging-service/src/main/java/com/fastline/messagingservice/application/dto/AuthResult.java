package com.fastline.messagingservice.application.dto;

public record AuthResult(String name, String email) {
	public static AuthResult of(String name, String email) {
		return new AuthResult(name, email);
	}
}
