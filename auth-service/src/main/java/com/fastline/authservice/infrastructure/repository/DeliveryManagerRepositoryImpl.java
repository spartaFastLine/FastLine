package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.DeliveryManager;
import com.fastline.authservice.domain.repository.DeliveryManagerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {
    private final JpaDeliveryManagerRepository jpaDeliveryManagerRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void save(DeliveryManager manager) {
        jpaDeliveryManagerRepository.save(manager);
    }

    @Override
    public long countAll() {
        return jpaDeliveryManagerRepository.count();
    }

    @Override
    public Optional<DeliveryManager> findById(Long userId) {
        return jpaDeliveryManagerRepository.findById(userId);
    }
}
