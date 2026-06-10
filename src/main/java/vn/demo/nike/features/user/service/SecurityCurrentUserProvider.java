package vn.demo.nike.features.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.demo.nike.infras.security.oauth.entity.OAuth2User;
import vn.demo.nike.features.user.entity.CustomUserPrincipal;
import vn.demo.nike.features.user.entity.User;
import vn.demo.nike.features.user.request.CurrentUserProvider;

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

        if(principal instanceof OAuth2User oAuth2User) {
            return oAuth2User.getId();
        }

        if (principal instanceof User user) {
            return user.getId();
        }

        return null;
    }
}
