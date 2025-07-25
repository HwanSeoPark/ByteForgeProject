package com.byteforge.admin.authority.controller;

import com.byteforge.admin.authority.dto.ModifyRoleRequest;
import com.byteforge.admin.authority.dto.SuspensionRequest;
import com.byteforge.admin.authority.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/authority")
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping
    public ResponseEntity findAllAuthorityUser(Pageable pageable, @RequestParam(required = false) String search) {
        return ResponseEntity.ok(authorityService.findAllAuthorityUser(pageable, search));
    }

    @GetMapping("/count")
    public ResponseEntity findNumberOfAllUsers(@RequestParam(required = false) String search) {
        return ResponseEntity.ok(authorityService.findNumberOfAllUsers(search));
    }

    @PatchMapping("/role")
    public ResponseEntity modifyAuthorityUserRole(@RequestBody ModifyRoleRequest request)
            throws AccountException {
        authorityService.modifyAuthorityUserRole(request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/suspension")
    public ResponseEntity modifySuspensionOfUse(@RequestBody SuspensionRequest request) throws AccountException {
        authorityService.modifySuspensionOfUse(request);

        return ResponseEntity.ok().build();
    }

}
