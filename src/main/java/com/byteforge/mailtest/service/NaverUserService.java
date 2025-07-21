package com.byteforge.mailtest.service;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NaverUserService {

    public Map<String, Object> getUserInfo(OAuth2User oAuth2User) {
        return oAuth2User.getAttributes();
    }
}
