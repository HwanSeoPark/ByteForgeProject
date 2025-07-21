package com.byteforge.admin.login.service;

import com.byteforge.account.user.constant.UserRole;
import com.byteforge.account.user.domain.User;
import com.byteforge.account.user.dto.LoginRequest;
import com.byteforge.account.user.exception.LoginException;
import com.byteforge.account.user.service.LoginService;
import com.byteforge.admin.login.dto.AdminLoginRequest;
import com.byteforge.admin.login.dto.AdminLoginResponse;
import com.byteforge.common.response.message.AccountMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final LoginService loginService;

    public AdminLoginResponse adminLogin(AdminLoginRequest request, HttpServletResponse response) {
        LoginRequest loginRequest = LoginRequest.createLoginRequest(request);

        User result = loginService.login(loginRequest, response);

        if(result.getRole() != UserRole.MANAGER) {
            throw new LoginException(AccountMessage.NOT_FOUNT_ACCOUNT);
        }

        return AdminLoginResponse.createResponse(result);
    }

}
