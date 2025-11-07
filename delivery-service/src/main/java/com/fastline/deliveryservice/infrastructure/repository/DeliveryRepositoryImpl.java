package com.fastline.deliveryservice.infrastructure.repository;

import com.fastline.deliveryservice.domain.entity.Delivery;
import com.fastline.deliveryservice.domain.repository.DeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

	private final JpaDeliveryRepository jpaDeliveryRepository;

	@Override
	public Delivery save(Delivery delivery) {
		return jpaDeliveryRepository.save(delivery);
	}

	@Override
	public Optional<Delivery> findById(UUID deliveryId) {
		return jpaDeliveryRepository.findById(deliveryId);
	}
}
