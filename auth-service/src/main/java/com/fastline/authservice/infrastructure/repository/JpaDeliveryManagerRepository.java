package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.DeliveryManager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDeliveryManagerRepository extends JpaRepository<DeliveryManager, Long> {
	long count();

	Optional<DeliveryManager> findById(Long userId);
}
