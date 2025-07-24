package com.byteforge.oauth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.byteforge.account.user.constant.UserType;
import com.byteforge.account.user.domain.User;
import com.byteforge.account.user.repository.LoginRepository;
import com.byteforge.oauth.dto.UserSession;
import com.byteforge.oauth.support.OAuthAttributes;


import java.time.LocalDate;
import java.util.Collections;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final LoginRepository loginRepository;
    private final HttpSession httpSession;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuthAttributes attributes = createOauthAttributes(userRequest);
        String kakaoregistrationId = userRequest.getClientRegistration().getRegistrationId();

        User user = saveOrUpdateUser(attributes, kakaoregistrationId);
        isValidAccount(user);

        httpSession.setAttribute("user", UserSession.of(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    public void isValidAccount(User user) {
        if(user.isSuspension() && user.getSuspensionDate().compareTo(LocalDate.now()) > 0) {
            throw new OAuth2AuthenticationException(new OAuth2Error("null"), "해당 계정은 " + user.getSuspensionDate() + " 일 까지 정지입니다. \n사유 : " + user.getSuspensionReason());
        }

        if(user.getUserType() == UserType.GENERAL_USER) {
            throw new OAuth2AuthenticationException(new OAuth2Error("null"), "해당 이메일로 일반 계정이 가입되어있습니다.");
        }

        if(user.isDelete()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("null"), "탈퇴한 사용자입니다.");
        }

        user.updateLoginDate();
    }

    public OAuthAttributes createOauthAttributes(OAuth2UserRequest userRequest) {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        return OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
    }

    public User saveOrUpdateUser(OAuthAttributes attributes, String kakaoregistrationId) {
        String email = attributes.getEmail();

        if (email == null) {
            if ("kakao".equals(kakaoregistrationId)) {
                // 카카오는 이메일이 없어도 임시 이메일 생성
                email = "kakao_" + attributes.getName() + "@kakao.local";
            } else {
                // 구글/네이버는 이메일 필수
                throw new OAuth2AuthenticationException(new OAuth2Error("null"),
                        "계정의 이메일을 찾을 수 없거나, 이메일 수집 여부에 동의하지 않았습니다.");
            }
        }
        final String finalEmail = email;

        return loginRepository.findByEmail(finalEmail)
                .orElseGet(() -> createAndSaveUser(finalEmail));
    }

    public User createAndSaveUser(String email) {
        User createUser = User.createOAuthUser(getIdWithoutEmail(email), email);

        return loginRepository.save(createUser);
    }

    public String getIdWithoutEmail(String email) {
        return email.split("@")[0];
    }
}