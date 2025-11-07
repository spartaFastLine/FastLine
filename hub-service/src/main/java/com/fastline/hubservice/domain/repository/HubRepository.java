package com.fastline.hubservice.domain.repository;

import com.fastline.hubservice.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // ← 추가
import java.util.UUID;

public interface HubRepository
        extends JpaRepository<Hub, UUID>, JpaSpecificationExecutor<Hub> { // ← 확장
    /**
     * 활성(소프트 삭제되지 않은) 허브 존재 여부
     * deleted_at IS NULL 조건을 포함한 편의 메서드
     */
    boolean existsByHubIdAndDeletedAtIsNull(UUID hubId);

    default boolean existsActiveById(UUID hubId) {
        return existsByHubIdAndDeletedAtIsNull(hubId);
    }
}
