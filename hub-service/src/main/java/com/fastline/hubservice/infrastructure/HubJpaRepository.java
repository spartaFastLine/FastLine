package com.fastline.hubservice.infrastructure;

import com.fastline.hubservice.domain.model.Hub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HubJpaRepository extends JpaRepository<Hub, UUID>, JpaSpecificationExecutor<Hub> {

    Optional<Hub> findByHubIdAndDeletedAtIsNull(UUID hubId);

    boolean existsByHubIdAndDeletedAtIsNull(UUID hubId);

    List<Hub> findAllByDeletedAtIsNull();

    @Query("SELECT h.centralHubId FROM Hub h WHERE h.hubId = :hubId AND h.deletedAt IS NULL")
    UUID findCentralHubIdByHubId(UUID hubId);
}
