package com.fastline.vendorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.fastline")
@EnableJpaAuditing
public class VendorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendorServiceApplication.class, args);
    }

}
