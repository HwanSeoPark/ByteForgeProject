package com.byteforge.security.jwt.service;


import com.byteforge.common.response.ResponseCode;
import com.byteforge.common.response.ResponseMessage;
import com.byteforge.security.exception.TokenForgeryException;
import com.byteforge.security.jwt.domain.RefreshToken;
import com.byteforge.security.jwt.dto.Token;
import com.byteforge.security.jwt.repository.RefreshTokenRepository;
import com.byteforge.security.jwt.support.CookieSupport;
import com.byteforge.security.jwt.support.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;
import java.util.NoSuchElementException;

import static com.byteforge.security.jwt.domain.RefreshToken.createRefreshToken;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Token login(Token token) {
        RefreshToken refreshToken = createRefreshToken(token);
        String loginUserEmail = refreshToken.getKeyEmail();

        if (refreshTokenRepository.existsByKeyEmail(loginUserEmail)) {
            refreshTokenRepository.deleteByKeyEmail(loginUserEmail);
        }

        refreshTokenRepository.save(refreshToken);

        return Token.builder()
                .grantType(token.getGrantType())
                .accessToken(token.getAccessToken())
                .refreshToken(refreshToken.getToken().toString())
                .key(loginUserEmail.toString())
                .build();
    }

    /**
     * Request에서 쿠키로 전달된 RefreshToken을 가져옴
     */
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length != 0) {
            return Arrays.stream(cookies)
                    .filter(c -> c.getName().equals("refreshToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElseThrow(() -> new SecurityException("인증되지 않은 사용자입니다."));
        }

        throw new SecurityException("인증되지 않은 사용자입니다.");
    }
    /**
     * 쿠키에서 가져온 RefreshToken을 DB에서 검증
     */
    public RefreshToken getRefreshToken(HttpServletRequest request) {
        String refreshTokenValue = getRefreshTokenFromHeader(request);

        return refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new TokenForgeryException("알 수 없는 RefreshToken 입니다."));
    }

    /**
     * RefreshToken 검증 및 새 AccessToken 발급
     */
    public ResponseMessage validateRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            RefreshToken storedToken = getRefreshToken(request);
            String newAccessToken = jwtTokenProvider.validateRefreshToken(storedToken);

            if (newAccessToken == null) {
                throw new TokenForgeryException("만료된 RefreshToken 입니다.");
            }

            response.addHeader("Set-Cookie",
                    CookieSupport.createAccessToken(newAccessToken).toString());

            return ResponseMessage.of(ResponseCode.CREATE_ACCESS_TOKEN);

        } catch (NoSuchElementException | TokenForgeryException e) {
            CookieSupport.deleteJwtTokenInCookie(response);
            throw new TokenForgeryException("변조되었거나 유효하지 않은 RefreshToken 입니다.");
        }
    }


    /**
     * 로그아웃: RefreshToken 삭제
     */
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

}

