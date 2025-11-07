package com.fastline.hubservice.domain.spec;


import com.fastline.hubservice.application.command.HubSearchCommand;
import com.fastline.hubservice.domain.model.Hub;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class HubSearchSpec {

    private HubSearchSpec() {}

    public static Specification<Hub> bySearch(HubSearchCommand command) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 공통: 소프트 삭제 제외
            predicates.add(cb.isNull(root.get("deletedAt")));

            // null/blank-safe 정규화
            String name = normalize(command.name());
            String address = normalize(command.address());

            if (name != null) {
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase(Locale.ROOT) + "%")
                );
            }
            if (address != null) {
                predicates.add(
                        cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase(Locale.ROOT) + "%")
                );
            }
            if (Objects.nonNull(command.centralHubId())) {
                predicates.add(cb.equal(root.get("centralHubId"), command.centralHubId()));
            }
            if (Objects.nonNull(command.isCentral())) {
                predicates.add(cb.equal(root.get("isCentral"), command.isCentral()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static String normalize(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
