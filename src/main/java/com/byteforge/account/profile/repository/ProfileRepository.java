package com.byteforge.account.profile.repository;

import com.byteforge.account.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> , CustomProfileRepository {

}
