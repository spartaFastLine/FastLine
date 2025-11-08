package com.fastline.gatewayservice.security;

import com.fastline.common.security.model.UserDetailsImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
public class ReaciveUserDetailsServiceImpl implements ReactiveUserDetailsService {
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return null;
    }

    public UserDetailsImpl loadUserInfo(Long userId, String username, String role, String hubId, String slackId) {
        UUID hubUUID = (hubId==null)? null:UUID.fromString(hubId);
        return new UserDetailsImpl(userId, username, "", role, hubUUID, slackId);
    }
}
