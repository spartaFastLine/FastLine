package com.fastline.vendorservice.infrastructure.external.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

    @GetMapping("/api/hubs/hub/{hubId}")
    boolean checkHubId(@PathVariable UUID hubId);
}
