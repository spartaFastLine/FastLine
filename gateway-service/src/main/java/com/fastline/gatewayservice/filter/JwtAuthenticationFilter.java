package com.fastline.gatewayservice.filter;

import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.gatewayservice.security.ReaciveUserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {
    private final JwtUtil jwtUtil;
    private final ReaciveUserDetailsServiceImpl userDetailsService;

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String token = resolveToken(exchange);
        if(token != null) {
            String rawToken = jwtUtil.substringToken(token); // Bearer 제거
            if (jwtUtil.validateToken(rawToken)) {
                Claims claims = jwtUtil.getUserInfoFromToken(rawToken);
                String username = claims.get("sub", String.class);
                if (username != null) {
                    var userDetails = userDetailsService.loadUserInfo(
                            claims.get("userId", Long.class),
                            username,
                            claims.get("auth", String.class),
                            claims.get("hubId", String.class),
                            claims.get("slackId", String.class));
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContext securityContext = new SecurityContextImpl(authentication);
                    return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                }
            }
        }
        return chain.filter(exchange);
    }
    private String resolveToken(ServerWebExchange request) {
        String header = request.getRequest().getHeaders().getFirst(JwtUtil.AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(JwtUtil.BEARER_PREFIX)) {
            return header;
        }
        return null;
    }
}
