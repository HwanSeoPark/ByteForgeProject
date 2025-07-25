package com.byteforge.admin.login.controller;

import com.byteforge.admin.login.dto.AdminLoginRequest;
import com.byteforge.admin.login.service.AdminLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoginController {

    private final AdminLoginService adminLoginService;

    @PostMapping("/login")
    public ResponseEntity adminLogin(@RequestBody AdminLoginRequest request, HttpServletResponse response) throws AccountException {

        return ResponseEntity.ok().body(adminLoginService.adminLogin(request, response));
    }

}
