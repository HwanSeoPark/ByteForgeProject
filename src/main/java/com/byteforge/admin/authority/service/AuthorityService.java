package com.byteforge.admin.authority.service;

import com.byteforge.account.user.domain.User;
import com.byteforge.account.user.repository.LoginRepository;
import com.byteforge.admin.authority.dto.AuthorityResponse;
import com.byteforge.admin.authority.dto.ModifyRoleRequest;
import com.byteforge.admin.authority.dto.SuspensionRequest;
import com.byteforge.admin.authority.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final LoginRepository loginRepository;

    public Long findNumberOfAllUsers(String search) {
        if(search == null) {
            return authorityRepository.countBy();
        }

        return authorityRepository.countByIdContaining(search);
    }

    public List<AuthorityResponse> findAllAuthorityUser(Pageable pageable, String search) {
        if(search == null) {
            return authorityRepository.findAllAuthorityUser(pageable);
        }

        return authorityRepository.findAllAuthorityUserById(pageable, search);
    }

    @Transactional
    public void modifyAuthorityUserRole(ModifyRoleRequest request) throws AccountException {
        User user = loginRepository.findById(request.getUserKey())
                .orElseThrow(() -> new AccountException("사용자 정보를 찾을 수 없습니다."));

        user.setRole(request.getUserRole());
    }

    @Transactional
    public void modifySuspensionOfUse(SuspensionRequest request) throws AccountException {
        User user = loginRepository.findById(request.getUserKey())
                .orElseThrow(() -> new AccountException("사용자 정보를 찾을 수 없습니다."));

        modifySuspensionDate(user, request.getSuspensionDate(), request.getReason());
    }

    private void modifySuspensionDate(User user, int date, String reason) {
        if(date > 0) {
            user.addSuspensionDate(date, reason);

            return;
        }

        user.minusSuspensionDate(date * -1);
    }
}
