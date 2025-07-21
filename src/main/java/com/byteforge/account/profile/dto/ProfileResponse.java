package com.byteforge.account.profile.dto;

import com.byteforge.account.profile.domain.Profile;
import com.byteforge.account.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponse {
    private String email;

    private String phone;

    private int options;

    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd" , timezone = "Asia/Seoul")
    private LocalDate joinDate;

    public static ProfileResponse createProfileResponse(Profile profile , User user) {
        return ProfileResponse.builder()
                .email(user.getEmail())
                .phone(profile.getPhone())
                .options(profile.getOptions())
                .joinDate(user.getJoinDate())
                .build();
    }
}
