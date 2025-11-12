package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.domain.model.HubPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubPathJpaRepository extends JpaRepository<HubPath, UUID> {

    // 기본 키 조회(soft delete 무시)
    Optional<HubPath> findByHubPathIdAndDeletedAtIsNull(UUID hubPathId);

    boolean existsByHubPathIdAndDeletedAtIsNull(UUID hubPathId);

    List<HubPath> findAllByDeletedAtIsNull();

    // 시작/종료 허브로 단건 조회 (soft delete 무시)
    Optional<HubPath> findByStartHub_HubIdAndEndHub_HubIdAndDeletedAtIsNull(UUID startHubId, UUID endHubId);

    // 활성 + soft delete 무시 조건들
    @Query("select hp from HubPath hp where hp.hubPathId = :id and hp.deletedAt is null and hp.active = true")
    Optional<HubPath> findActiveById(@Param("id") UUID id);

    @Query("select hp from HubPath hp where hp.deletedAt is null and hp.active = true")
    List<HubPath> findAllActive();

    @Query("select hp from HubPath hp where hp.startHub.hubId = :startId and hp.endHub.hubId = :endId and hp.deletedAt is null and hp.active = true")
    Optional<HubPath> findActiveByStartAndEnd(@Param("startId") UUID startId, @Param("endId") UUID endId);
}
