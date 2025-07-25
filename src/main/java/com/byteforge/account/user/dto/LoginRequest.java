package com.byteforge.account.user.dto;

import com.byteforge.admin.login.dto.AdminLoginRequest;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String id;
    private String email;
    private String password;
    private String checkPassword;

    public static LoginRequest createLoginRequest(AdminLoginRequest request) {
        return LoginRequest.builder()
                .id(request.getId())
                .password(request.getPassword())
                .checkPassword(request.getPassword())
                .build();
    }

}
