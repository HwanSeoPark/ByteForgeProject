package com.byteforge.admin.authority.dto;

import com.byteforge.account.user.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AuthorityResponse {

    private long userKey;

    private String id;

    private LocalDate joinDate;

    private LocalDate lastLoginDate;

    private String suspensionReason;

    private LocalDate suspensionDate;

    private boolean isSuspension;

    private UserRole userRole;

}
