package com.byteforge.security.jwt.support;

import com.byteforge.account.user.constant.UserRole;
import com.byteforge.security.exception.TokenForgeryException;
import com.byteforge.security.jwt.domain.RefreshToken;
import com.byteforge.security.jwt.dto.Token;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService loginService;

    private String secretKey = "byteForgeSecretKey";
    private String refreshKey = "byteForgeSecretKey";
    private long accessTokenValidTime = 30 * 60 * 1000L;
    private long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L;
    private Key key;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        refreshKey = Base64.getEncoder().encodeToString(refreshKey.getBytes());
    }

    public Token createJwtToken(String userPk, UserRole role) {
        Claims claims = jwtClaims(userPk, role);

        String accessToken = createToken(claims, accessTokenValidTime, secretKey);
        String refreshToken = createToken(claims, refreshTokenValidTime, refreshKey);

        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(userPk)
                .grantType("Bearer")
                .build();
    }

    private Claims jwtClaims(String userPk, UserRole role) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", role);
        return claims;
    }

    private String createToken(Claims claims, long validTime, String key) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public String createAccessToken(Claims claims) {
        return createToken(claims, accessTokenValidTime, secretKey);
    }

    public String createRefreshToken(Claims claims) {
        return createToken(claims, refreshTokenValidTime, refreshKey);
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loginService.loadUserByUsername(getUserIdFromToken(token, secretKey));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserIdFromToken(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateAccessToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("JWT가 만료되었습니다.");
            return false;
        } catch (SignatureException e) {
            throw new TokenForgeryException("토큰 위조 또는 알 수 없는 토큰입니다.");
        }
    }

    public String recreationAccessToken(String userEmail, Object roles) {
        Claims claims = recreClaims(userEmail, roles);
        return createToken(claims, accessTokenValidTime, secretKey);
    }

    public String validateRefreshToken(RefreshToken refreshToken) {
        String token = refreshToken.getToken();

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(refreshKey)
                .parseClaimsJws(token);

        if (!claims.getBody().getExpiration().before(new Date())) {
            return recreationAccessToken(claims.getBody().getSubject(), claims.getBody().get("roles"));
        }
        return null;
    }



    private Claims recreClaims(String userEmail, Object roles) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("roles", roles);
        return claims;
    }
}



