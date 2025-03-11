package com.practice.config.filters;

import com.practice.config.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtils jwtUtils;

    public CustomOAuth2SuccessHandler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String token = jwtUtils.generateJwtToken(authentication);
        log.info("🔑 JWT generado: {}", token);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // Previene acceso via JS
        cookie.setSecure(true); // Solo en HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 días

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);

        String frontendUrl = "https://iupi-six.vercel.app/app/home"; // Producción
        if ("localhost".equals(request.getServerName())) {
            frontendUrl = "http://localhost:3000/app/home"; // Desarrollo
        }

        response.sendRedirect(frontendUrl);
    }
}
