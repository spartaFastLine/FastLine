package com.fastline.common.security.filter;

import com.fastline.common.security.jwt.JwtUtil;
import com.fastline.common.security.model.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
public class AuthoriztionFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtUtil.resolveToken(request);
            token = jwtUtil.substringToken(token); // Bearer 제거
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            String username = claims.get("sub", String.class);
            if (username != null) {
                //사용자 정보 조회 - UserDetailsServiceImpl 각 서비스에 필요한 권한에 따라 커스텀 구현
                var userDetails = userDetailsService.loadUserInfo(claims);
                logger.debug("uerDetails : "+userDetails.toString());
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ex) {
            // 인증 실패해도 요청은 다음 필터로 넘겨져서 정상적으로 401/403 처리됨.
            logger.error(ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
