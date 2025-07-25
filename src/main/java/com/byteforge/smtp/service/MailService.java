package com.byteforge.smtp.service;

import com.byteforge.common.response.message.MailMessage;
import com.byteforge.smtp.dto.CertRequest;
import com.byteforge.smtp.entity.MailCert;
import com.byteforge.smtp.exception.MailException;
import com.byteforge.smtp.repository.MailCertRepository;
import com.byteforge.smtp.util.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailUtil mailUtil;
    private final MailCertRepository mailCertRepository;

    @Transactional
    public void sendMail(CertRequest request) {
        MailCert mailCert = createVerification(request.getEmail());

        if (!mailUtil.sendMail(request.getEmail(), mailCert.getVerificationCode())) {
            throw new MailException(MailMessage.SMTP_SERVER_ERROR);
        }
    }

    private MailCert createVerification(String id) {
        String code = createVerificationCode();

        MailCert mailCert = createVerificationCode(id, code);

        return mailCertRepository.save(mailCert);
    }

    private MailCert createVerificationCode(String id, String code) {
        MailCert mailCert = mailCertRepository.findById(id)
                .orElseGet(() -> MailCert.createMailCert(id, code));

        return mailCert;
    }

    @Transactional
    public void checkVerificationCode(CertRequest request) {
        if(isCorrectVerificationCode(request)) {
            mailCertRepository.deleteById(request.getEmail());
        }
    }

    private boolean isCorrectVerificationCode(CertRequest request) {
        MailCert mailCert = mailCertRepository.findById(request.getEmail())
                .orElseThrow(() -> new MailException(MailMessage.UNUSUAL_APPROACH));

        if(!mailCert.isCorrectVerificationCode(request.getCode())) {
            throw new MailException(MailMessage.NOT_MATCHED_CODE);
        }

        return true;
    }

    private String createVerificationCode() {
        String code = UUID.randomUUID().toString();

        return code;
    }

}