package com.byteforge.security.config;

import com.byteforge.account.user.constant.UserRole;
import com.byteforge.admin.visitant.util.SingleVisitInterceptor;
import com.byteforge.common.exception.FilterExceptionHandler;
import com.byteforge.oauth.service.CustomOAuth2UserService;
import com.byteforge.oauth.support.CustomAuthenticationFailureHandler;
import com.byteforge.oauth.support.OAuth2AuthenticationSuccessHandler;
import com.byteforge.security.jwt.support.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter authenticationFilter;
    private final CustomOAuth2UserService oauth2UserService;
    private final SingleVisitInterceptor singleVisitInterceptor;
    private final OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/admin/**").hasRole(UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/admin/report/**" , "/admin/login", "/mail/**", "/logins", "/registers", "/oauth/token", "/user/logout").permitAll()
                .requestMatchers( "/admin/**").hasRole(UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.POST, "/**").hasAnyRole(UserRole.USER.name(), UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.PATCH, "/posts/views/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/**").hasAnyRole(UserRole.USER.name(), UserRole.MANAGER.name())
                .requestMatchers(HttpMethod.PATCH, "/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/**").hasAnyRole(UserRole.USER.name(), UserRole.MANAGER.name())
                .and()
                // Configures authentication support using an OAuth 2.0 and/or OpenID Connect 1.0 Provider.
                .oauth2Login().loginPage("/authorization/denied")
                // loginPage가 리턴하는 OAuth2LoginConfigurer는 다음과 같음.
// public final class OAuth2LoginConfigurer<B extends HttpSecurityBuilder<B>>
// extends AbstractAuthenticationFilterConfigurer<B, org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer<B>, OAuth2LoginAuthenticationFilter>
                // OAuth2LoginAuthenticationFilter
                .successHandler(oauth2AuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .userInfoEndpoint().userService(oauth2UserService);

        http.addFilterBefore(new FilterExceptionHandler(),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(singleVisitInterceptor,
                UsernamePasswordAuthenticationFilter.class
        );


        http.addFilterBefore(authenticationFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}