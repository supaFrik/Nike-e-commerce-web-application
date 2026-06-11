package vn.demo.nike.features.user.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import vn.demo.nike.infras.security.oauth.entity.NikeOidcUser;
import vn.demo.nike.infras.security.oauth.entity.NikeOAuth2User;
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

        if (principal instanceof NikeOAuth2User nikeOAuth2User) {
            return nikeOAuth2User.getId();
        }

        if (principal instanceof NikeOidcUser nikeOidcUser) {
            return nikeOidcUser.getId();
        }

        if (principal instanceof Jwt jwt) {
            return parseUserId(jwt.getSubject());
        }

        if (principal instanceof User user) {
            return user.getId();
        }

        return null;
    }

    private Long parseUserId(String subject) {
        if (subject == null || subject.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
