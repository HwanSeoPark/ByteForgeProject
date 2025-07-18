package com.byteforge.admin.authority.dto;

import com.byteforge.account.user.constant.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyRoleRequest {

    private long userKey;

    private UserRole userRole;

}
