package com.fastline.hubservice.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HubController {

	@GetMapping("/hub")
	public String hello() {
		return "Hello World";
	}
}
