package com.fastline.hubservice.domain.repository;

import com.fastline.hubservice.domain.model.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Domain Repository for Hub (DDD)
 * - 도메인 계층은 Spring Data 등 인프라에 의존하지 않도록 순수 인터페이스로 정의한다.
 * - 실제 구현은 infrastructure 계층(JpaHubRepository 등)에서 제공한다.
 */
public interface HubRepository {

    /** 활성(soft-delete 아님) 허브 단건 조회 */
    Optional<Hub> findActiveById(UUID hubId);

    /** 활성(soft-delete 아님) 허브 존재 여부 */
    boolean existsActiveById(UUID hubId);

    /** 주어진 허브의 소속 거점 허브 ID 조회 (멤버 허브일 경우에만 의미) */
    UUID findCentralIdOf(UUID hubId);

    /** 활성 허브 전체 조회 (그래프 구성 등) */
    List<Hub> findAllActive();

    /** 저장(업서트 포함) */
    Hub save(Hub hub);

    Optional<Object> findById(UUID hubId);

    long count(Specification<Hub> childSpec);

    Page<Hub> findAll(Specification<Hub> spec, Pageable pageable);
}
