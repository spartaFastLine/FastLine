package com.fastline.deliveryservice.infrastructure.config;

import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import com.fastline.deliveryservice.infrastructure.repository.DeliveryRepositoryAdapter;
import com.fastline.deliveryservice.infrastructure.repository.JpaDeliveryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    public DeliveryRepository deliveryRepository(JpaDeliveryRepository jpaDeliveryRepository) {
        return new DeliveryRepositoryAdapter(jpaDeliveryRepository);
    }
}
