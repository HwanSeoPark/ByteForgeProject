package com.byteforge.admin.authority.repository;

import com.byteforge.account.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<User, Long>, CustomAuthorityRepository {

    long countBy();

    long countByIdContaining(String id);

}