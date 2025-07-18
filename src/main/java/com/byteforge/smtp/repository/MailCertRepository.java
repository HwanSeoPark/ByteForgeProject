package com.byteforge.smtp.repository;

import com.byteforge.smtp.entity.MailCert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailCertRepository extends JpaRepository<MailCert , Long> {

    Optional<MailCert> findById(String id);

    long deleteById(String id);

}
