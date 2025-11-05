package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.model.UserRole;
import com.fastline.authservice.domain.model.UserStatus;
import com.fastline.authservice.domain.repository.UserRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.fastline.authservice.domain.model.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }
    @Override
    public Optional<User> findByUsername(String username) {return jpaUserRepository.findByUsername(username); }
    @Override
    public Optional<User> findById(Long id) {return jpaUserRepository.findById(id); }

    @Override
    public void save(User user) {jpaUserRepository.save(user);}

    @Override
    public List<User> findUsers(String username, UUID hubId, String role, String status) {
        return jpaQueryFactory.selectFrom(user)
                .where(
                        eqUsername(username),
                        eqhubId(hubId),
                        eqRole(role),
                        eqStatus(status)
                )
                .fetch();
    }

    private BooleanExpression eqUsername(String username) {
        return StringUtils.hasText(username) ? user.username.eq(username) : null;
    }
    private BooleanExpression eqhubId(UUID hubId) {
        return hubId != null ? user.hubId.eq(hubId) : null;
    }
    private BooleanExpression eqRole(String role) {
        return StringUtils.hasText(role) ? user.role.eq(UserRole.valueOf(role)) : null;
    }
    private BooleanExpression eqStatus(String status) {
        return StringUtils.hasText(status) ? user.status.eq(UserStatus.valueOf(status)) : null;
    }


}
