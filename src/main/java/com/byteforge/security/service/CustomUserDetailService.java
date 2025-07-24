package com.byteforge.security.service;

import com.byteforge.account.user.domain.User;
import com.byteforge.account.user.repository.LoginRepository;
import com.byteforge.common.response.message.AccountMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    public final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User result = loginRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(AccountMessage.NOT_FOUNT_ACCOUNT.getMessage()));

        return result;
    }
}
