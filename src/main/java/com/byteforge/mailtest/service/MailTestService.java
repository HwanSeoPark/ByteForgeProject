package com.byteforge.mailtest.service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailTestService {

    private final JavaMailSender mailSender;

    public void sendTestMail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("SMTP 연결 테스트");
        message.setText("안녕하세요! SMTP가 정상적으로 연결되었습니다.");
        mailSender.send(message);
    }
}