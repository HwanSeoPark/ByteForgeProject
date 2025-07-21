package com.byteforge.security.jwt.support;

import com.byteforge.security.jwt.dto.Token;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CookieSupport {

    @Value("${server.url}")
    private static String DOMAIN_URL;

    // accessToken을 담는 ResponseCookie 생성
    public static ResponseCookie createAccessToken(String access) {
        return ResponseCookie.from("accessToken", access)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 60 * 1000)
                .build();
    }

    // refreshToken을 담는 ResponseCookie 생성
    public static ResponseCookie createRefreshToken(String refresh) {
        return ResponseCookie.from("refreshToken", refresh)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(14 * 24 * 60 * 60 * 1000)
                .build();
    }


    public static void setCookieFromJwt(Token token, HttpServletResponse response) {
        ResponseCookie accessCookie = createAccessToken(token.getAccessToken());
        ResponseCookie refreshCookie = createRefreshToken(token.getRefreshToken());

        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        ResponseCookie deleteSessionCookie = ResponseCookie.from("JSESSIONID", "")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .maxAge(0) // 0으로 설정해 바로 삭제
                .build();

        response.addHeader("Set-Cookie", deleteSessionCookie.toString());

    }


    // JWT 관련 쿠키 삭제
    public static void deleteJwtTokenInCookie(HttpServletResponse response) {
        ResponseCookie accessDelete = ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 60 * 1000)
                .build();

        ResponseCookie refreshDelete = ResponseCookie.from("refreshToken", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(14 * 24 * 60 * 60 * 1000)
                .build();

        response.addHeader("Set-Cookie", accessDelete.toString());
        response.addHeader("Set-Cookie", refreshDelete.toString());
    }

}