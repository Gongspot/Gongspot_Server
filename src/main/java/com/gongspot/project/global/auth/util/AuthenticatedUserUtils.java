package com.gongspot.project.global.auth.util;

import com.gongspot.project.common.code.status.ErrorStatus;
import com.gongspot.project.common.exception.BusinessException;
import com.gongspot.project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticatedUserUtils {

    private final UserRepository userRepository;

    public Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String userIdStr) {
            return Long.valueOf(userIdStr);
        }

        throw new BusinessException(ErrorStatus._UNAUTHORIZED);
    }
}
