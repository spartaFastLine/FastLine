package com.fastline.authservice.infrastructure.filter;

import com.fastline.authservice.domain.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            if (token != null) {
                String rawToken = jwtUtil.substringToken(token); // Bearer 제거
                if (jwtUtil.validateToken(rawToken)) {
                    Claims claims = jwtUtil.getUserInfoFromToken(rawToken);
                    String username = claims.getSubject();
                    if (username != null) {
                        var userDetails = userDetailsService.loadUserByUsername(username);
                        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            }
        } catch (Exception ex) {
            // 인증 실패해도 요청은 다음 필터로 넘겨져서 정상적으로 401/403 처리됨.
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(JwtUtil.BEARER_PREFIX)) {
            return header;
        }
        // 쿠키에서 토큰 확인 (디코딩 포함된 유틸 사용)
        String cookieToken = jwtUtil.getTokenFromRequest(request);
        return cookieToken;
    }
}