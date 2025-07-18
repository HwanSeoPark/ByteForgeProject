package com.byteforge.oauth.support;

import com.byteforge.account.user.constant.UserRole;
import com.byteforge.oauth.dto.UserSession;
import com.byteforge.security.jwt.dto.Token;
import com.byteforge.security.jwt.support.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.byteforge.security.jwt.support.CookieSupport.*;

@Component
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpSession httpSession;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserSession user = (UserSession) httpSession.getAttribute("user");

        if (user == null) {
            getRedirectStrategy().sendRedirect(request, response, createRedirectUrl(clientUrl + "/oauth2/disallowance"));

            return;
        }

        Token token = jwtTokenProvider.createJwtToken(user.getId(), UserRole.USER);
        setCookieFromJwt(token , response);

        httpSession.removeAttribute("user");

        getRedirectStrategy().sendRedirect(request, response, createRedirectUrl(clientUrl));
    }

    public String createRedirectUrl(String url) {
        return UriComponentsBuilder.fromUriString(url).build().toUriString();
    }
}
