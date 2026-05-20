package vn.demo.nike.features.identity.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.demo.nike.features.identity.user.entity.CustomUserPrincipal;
import vn.demo.nike.features.identity.user.entity.GuestIdentityCookieManager;
import vn.demo.nike.features.identity.user.entity.ShopperContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.demo.nike.features.identity.user.request.CurrentShopperProvider;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CookieBackedCurrentShopperProvider implements CurrentShopperProvider {

    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final GuestIdentityCookieManager guestIdentityCookieManager;

    @Override
    public ShopperContext getCurrentShopperContext() {
        // TODO 1: Call extractAuthenticatedUserId() first.
        // TODO 2: If userId exists, return an authenticated ShopperContext and do not create a guest cookie.
        // TODO 3: If userId is null, read guest_id from GuestIdentityCookieManager.
        // TODO 4: If guest_id is missing or blank, generate a new UUID string.
        // TODO 5: Persist the new guest_id with GuestIdentityCookieManager.writeGuestId(...).
        // TODO 6: Return a guest ShopperContext containing only guestId.
        // TODO 7: Keep this method free from cart/checkout business rules; it should only resolve identity.
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
        // TODO 1: Read Authentication from SecurityContextHolder.
        // TODO 2: Return null when authentication is missing.
        // TODO 3: Return null when authentication is not authenticated.
        // TODO 4: Return null when authentication is AnonymousAuthenticationToken.
        // TODO 5: Inspect authentication.getPrincipal().
        // TODO 6: If principal is CustomUserPrincipal, return its id.
        // TODO 7: If this repo also supports User as principal, decide whether to support that here too.
        // TODO 8: Return null for any unsupported principal type.
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
