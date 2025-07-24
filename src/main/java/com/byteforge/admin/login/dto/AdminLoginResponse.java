package com.byteforge.admin.login.dto;

import com.byteforge.account.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminLoginResponse {

    private String id;
    private String name;
    private String message;


    public static AdminLoginResponse createResponse(User user) {
        return AdminLoginResponse.builder()
                .id(user.getId())
                .name(user.getUsername())
                .message("관리자 로그인에 성공하였습니다.")
                .build();
    }

}
