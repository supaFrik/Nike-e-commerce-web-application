package vn.demo.nike.features.identity.user.entity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class GuestIdentityCookieManager {
    public static final String GUEST_ID_COOKIE = "guest_id";
    private static final int MAX_AGE = 60 * 60 * 24 * 30;

    public String readGuestId(HttpServletRequest request) {
        // TODO 1: Read cookies from the request.
        // TODO 2: Return null when the request has no cookies.
        // TODO 3: Find the cookie named GUEST_ID_COOKIE.
        // TODO 4: Return null when the cookie value is missing or blank.
        // TODO 5: Return the guest id value when present.
        // TODO 6: Do not create a new cookie inside this method; creation belongs to the provider.
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(GUEST_ID_COOKIE)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void writeGuestId(HttpServletRequest request, HttpServletResponse response, String guestId) {
        // TODO 1: Validate guestId is not null or blank before writing the cookie.
        // TODO 2: Create a cookie named GUEST_ID_COOKIE with the provided guestId.
        // TODO 3: Set HttpOnly so frontend JavaScript cannot read the guest identity.
        // TODO 4: Set path to "/" so cart, checkout, and product pages share the same guest id.
        // TODO 5: Set max age to MAX_AGE.
        // TODO 6: Set secure based on request.isSecure() for local/prod compatibility.
        // TODO 7: Add the cookie to the response.
        // TODO 8: Later, consider SameSite=Lax if you add a response-cookie helper.
        if(guestId == null || guestId.isBlank()) return ;
        Cookie cookie = new Cookie(GUEST_ID_COOKIE, guestId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }
}
