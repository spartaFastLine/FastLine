package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.domain.model.HubPath;
import com.fastline.hubservice.domain.repository.HubPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * HubPath JPA 어댑터
 * - 인프라스트럭처 계층에서 Spring Data JPA 리포지토리를 감싸 도메인 친화적 API 제공
 * - 소프트 삭제(deletedAt) 조건을 기본으로 하는 편의 메서드 노출
 */
@Repository
@RequiredArgsConstructor
public class HubPathJpaAdapter implements HubPathRepository {

    private final HubPathJpaRepository hubPathJpaRepository; // Spring Data 기반 Repo 주입

    @Override
    public Optional<HubPath> findByHubPathIdAndDeletedAtIsNull(UUID hubPathId) {
        return hubPathJpaRepository.findByHubPathIdAndDeletedAtIsNull(hubPathId);
    }

    @Override
    public boolean existsByHubPathIdAndDeletedAtIsNull(UUID hubPathId) {
        return hubPathJpaRepository.existsByHubPathIdAndDeletedAtIsNull(hubPathId);
    }

    @Override
    public List<HubPath> findAllByDeletedAtIsNull() {
        return hubPathJpaRepository.findAllByDeletedAtIsNull();
    }

    @Override
    public Optional<HubPath> findByStartHub_HubIdAndEndHub_HubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId) {
        return hubPathJpaRepository.findByStartHub_HubIdAndEndHub_HubIdAndDeletedAtIsNull(startHubId, endHubId);
    }

    @Override
    public Optional<HubPath> findActiveById(UUID hubPathId) {
        return hubPathJpaRepository.findActiveById(hubPathId);
    }


    @Override
    public List<HubPath> findAllActive() {
        return hubPathJpaRepository.findAllActive();
    }

    @Override
    public HubPath save(HubPath hubPath) {
        return hubPathJpaRepository.save(hubPath);
    }

    @Override
    public Optional<HubPath> findActiveByStartAndEnd(UUID startHubId, UUID endHubId) {
        return hubPathJpaRepository.findActiveByStartAndEnd(startHubId, endHubId);
    }


}
