package com.byteforge.admin.authority.repository;

import com.byteforge.admin.authority.dto.AuthorityResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomAuthorityRepository {

    List<AuthorityResponse> findAllAuthorityUser(Pageable pageable);

    List<AuthorityResponse> findAllAuthorityUserById(Pageable pageable, String search);

}
