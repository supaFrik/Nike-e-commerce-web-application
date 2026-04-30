package vn.demo.nike.features.identity.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.demo.nike.features.identity.user.domain.CustomUserPrincipal;
import vn.demo.nike.features.identity.user.domain.User;
import vn.demo.nike.features.identity.user.request.CurrentUserProvider;

@Component
public class SecurityCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserPrincipal custom) {
            return custom.getId();
        }

        if (principal instanceof User user) {
            return user.getId();
        }

        return null;
    }
}
