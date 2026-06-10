package vn.demo.nike.features.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.demo.nike.features.user.entity.CustomUserPrincipal;
import vn.demo.nike.features.user.entity.GuestIdentityCookieManager;
import vn.demo.nike.features.user.entity.ShopperContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.demo.nike.features.user.request.CurrentShopperProvider;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CookieBackedCurrentShopperProvider implements CurrentShopperProvider {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final GuestIdentityCookieManager guestIdentityCookieManager;

    @Override
    public ShopperContext getCurrentShopperContext() {
        Long userId = extractAuthenticatedUserId();

        // Logged in user
        if(userId != null){
            return new ShopperContext(
                    true,
                    userId,
                    null
            );
        }

        // Guest user
        String guestId = guestIdentityCookieManager.readGuestId(httpServletRequest);
        if(guestId == null || guestId.isBlank()) {
            guestId = UUID.randomUUID().toString();

            guestIdentityCookieManager.writeGuestId(
                    httpServletRequest,
                    httpServletResponse,
                    guestId
            );
        }
        return new ShopperContext(
                true,
                userId,
                guestId
        );
    }

    private Long extractAuthenticatedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
            return null;
        }
        if(authentication == null) {
            return null;
        }
        if(!authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if(principal instanceof CustomUserPrincipal userPrincipal) {
            return userPrincipal.getId();
        }

        return null;
    }
}
