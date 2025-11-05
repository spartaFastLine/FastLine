package com.fastline.authservice.infrastructure.repository;

import com.fastline.authservice.domain.model.User;
import com.fastline.authservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

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


}
