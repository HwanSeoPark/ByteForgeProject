package com.byteforge.security.jwt.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
    private String key;
    private String grantType; // Bearer Token
}
