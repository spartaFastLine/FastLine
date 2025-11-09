package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.DeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager,Long> {
    long count();
    Optional<DeliveryManager> findById(Long userId);

}
