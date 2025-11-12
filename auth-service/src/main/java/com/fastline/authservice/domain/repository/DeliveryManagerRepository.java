package com.fastline.authservice.domain.repository;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.model.DeliveryManagerType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryManagerRepository {
	void save(DeliveryManager deliveryManager);

	long countAll();

	Optional<DeliveryManager> findById(Long userId);

	Page<DeliveryManager> findDeliveryManagers(
			String username,
			UUID hubId,
			String deliveryManagerType,
			Long number,
			String userStatus,
			boolean isActive,
			Pageable pageable);

    Optional<DeliveryManager> assignDeliveryManager(UUID hubId, DeliveryManagerType type);

}
