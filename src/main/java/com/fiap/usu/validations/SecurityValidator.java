package com.fiap.usu.validations;

import com.fiap.usu.dtos.auth.UserPrincipalDto;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityValidator {

    public void validateUserAccess(Long idToAccess) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthorizationDeniedException(ValidationMessages.NOT_AUTHENTICATED);
        }

        UserPrincipalDto principal = (UserPrincipalDto) authentication.getPrincipal();

        boolean isAdmin = principal.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!principal.getId().equals(idToAccess) && !isAdmin) {
            throw new AuthorizationDeniedException(ValidationMessages.NO_ACCESS);
        }
    }

    public void validateUserAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new AuthorizationDeniedException(ValidationMessages.NO_ACCESS);
        }
    }
}
