package com.byteforge.security.jwt.support;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.byteforge.common.response.ResponseCode;
import com.byteforge.security.jwt.service.JwtService;

import static com.byteforge.common.exception.FilterExceptionHandler.setSuccessResponse;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = getAccessTokenFromCookies(httpRequest);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateAccessToken(token)) {
            // 유효한 accessToken 이면 인증 객체 생성
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("유효한 accessToken으로 인증 완료.");
        } else if (StringUtils.hasText(token)) {
            // accessToken은 있는데 유효하지 않은 경우 → refreshToken으로 재발급 시도
            log.info("accessToken이 유효하지 않음. refreshToken으로 재발급 시도.");
            jwtService.validateRefreshToken(httpRequest, httpResponse);

            setSuccessResponse(httpResponse, ResponseCode.CREATE_ACCESS_TOKEN);
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 쿠키에서 accessToken 추출
     */
    private String getAccessTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            return Arrays.stream(cookies)
                    .filter(cookie -> "accessToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }
}