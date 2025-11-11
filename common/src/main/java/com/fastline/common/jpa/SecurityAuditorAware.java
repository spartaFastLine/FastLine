package com.fastline.common.jpa;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import com.fastline.common.security.model.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<Long> {
    @Override
    @NonNull
    public Optional<Long> getCurrentAuditor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth==null || !auth.isAuthenticated()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            return Optional.ofNullable(userDetails.getUserId());
        }
        return Optional.empty();

    }
}
