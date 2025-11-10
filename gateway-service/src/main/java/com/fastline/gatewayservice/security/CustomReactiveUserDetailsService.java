package com.fastline.gatewayservice.security;

import com.fastline.common.security.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public interface CustomReactiveUserDetailsService {
    Mono<UserDetailsImpl> loadUserInfo(Claims claims);
}
