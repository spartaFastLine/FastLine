package com.fastline.deliveryservice.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeliveryController {

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }
}
