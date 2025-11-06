package com.fastline.hubservice.application.service;

import com.fastline.hubservice.application.command.CreateHubCommand;
import com.fastline.hubservice.domain.model.Hub;
import com.fastline.hubservice.domain.repository.HubRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
