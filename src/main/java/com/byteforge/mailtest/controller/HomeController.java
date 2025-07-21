package com.byteforge.mailtest.controller;


import com.byteforge.mailtest.service.NaverUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NaverUserService naverUserService;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            model.addAttribute("user", naverUserService.getUserInfo(oAuth2User));
        }
        return "home"; // templates/home.html
    }

}
