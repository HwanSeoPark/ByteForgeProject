package com.byteforge.account.profile.repository;

import com.byteforge.account.profile.dto.StatisticsResponse;

public interface CustomProfileRepository {

    StatisticsResponse getStatisticsOfUser(String userId);

}
