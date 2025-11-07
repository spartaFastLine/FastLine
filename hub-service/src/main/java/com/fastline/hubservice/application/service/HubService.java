package com.fastline.hubservice.application.service;

import com.fastline.hubservice.application.command.CreateHubCommand;
import com.fastline.hubservice.application.command.HubSearchCommand;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.repository.HubRepository;
import com.fastline.hubservice.domain.spec.HubSearchSpec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HubService {
    private final HubRepository hubRepository;
    //private final HubDomainService hubDomainService;

    @Transactional
    public UUID createHub(CreateHubCommand command) {
        log.info("허브 생성 시작 : hubId={}", command);

        var spec = new Hub.CreateSpec(
                command.getCentralHubId(),
                command.isCentral(), // null-safe
                command.getName(),
                command.getAddress(),
                command.getLatitude(),
                command.getLongitude()
        );
        if (!spec.isCentral()) {
            UUID parentId = spec.centralHubId();
            if (parentId == null || !hubRepository.existsActiveById(parentId)) {
                //throw new BusinessException("상위(중앙) 허브가 존재하지 않거나 비활성입니다: " + parentId);
            }
        }
        Hub hub = Hub.create(spec);

        hubRepository.save(hub);

        log.info("허브 생성 완료: hubId={}", hub.getHubId());
        return hub.getHubId();
    }

    @Transactional(readOnly = true)
    public Hub getHub(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브가 존재하지 않습니다: " + hubId));
    }
    // 클래스 마지막 닫는 괄호 직전에 추가
    @Transactional(readOnly = true)
    public Page<Hub> searchHubs(HubSearchCommand command, Pageable pageable) {
        Specification<Hub> spec = HubSearchSpec.bySearch(command);
        return hubRepository.findAll(spec, pageable);
    }

    /**
     * 허브 소프트 삭제
     * - 중앙 허브이고 활성 하위 허브가 1개 이상 존재하면 삭제 불가
     * - 실제 삭제 대신 deletedAt을 세팅
     */
    @Transactional
    public void softDeleteHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브가 존재하지 않습니다: " + hubId));

        // 이미 삭제된 경우는 조용히 종료하거나 정책에 따라 예외를 던질 수 있음
        if (hub.getDeletedAt() != null) {
            return;
        }

        // 중앙 허브 보호: 활성 하위 허브 존재 여부 확인
        if (hub.isCentral()) {
            Specification<Hub> childSpec = (root, query, cb) -> {
                Predicate notDeleted = cb.isNull(root.get("deletedAt"));
                Predicate childOfThis = cb.equal(root.get("centralHubId"), hubId);
                return cb.and(notDeleted, childOfThis);
            };
            long childrenCnt = hubRepository.count(childSpec);
            if (childrenCnt > 0) {
                throw new IllegalStateException("하위 허브가 존재하여 중앙 허브를 삭제할 수 없습니다. hubId=" + hubId + ", childrenCnt=" + childrenCnt);
            }
        }

        // 소프트 삭제
        hub.markDeleted();
        hubRepository.save(hub);
        log.info("허브 소프트 삭제 완료: hub_id={}", hubId);
    }
}
