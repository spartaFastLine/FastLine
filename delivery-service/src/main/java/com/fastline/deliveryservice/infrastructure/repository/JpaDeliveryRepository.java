package com.fastline.deliveryservice.infrastructure.repository;

import com.fastline.deliveryservice.domain.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaDeliveryRepository extends JpaRepository<Delivery, UUID> {
}
