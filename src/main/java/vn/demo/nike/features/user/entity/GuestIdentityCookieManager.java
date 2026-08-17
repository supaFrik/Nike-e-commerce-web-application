package vn.demo.nike.features.user.entity;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class GuestIdentityCookieManager {
    public static final String GUEST_ID_COOKIE = "guest_id";
    private static final int MAX_AGE = 60 * 60 * 24 * 30;

    public String readGuestId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (GUEST_ID_COOKIE.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isBlank()) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void writeGuestId(HttpServletRequest request, HttpServletResponse response, String guestId) {
        if(guestId == null || guestId.isBlank()) return ;
        Cookie cookie = new Cookie(GUEST_ID_COOKIE, guestId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        cookie.setSecure(request.isSecure());
        response.addCookie(cookie);
    }
}
