package com.byteforge.admin.authority.dto;

import lombok.Getter;

@Getter
public class SuspensionRequest {

    private long userKey;

    private int suspensionDate;

    private String reason;

}
