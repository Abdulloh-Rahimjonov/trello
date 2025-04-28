package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail , String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abdukayumsss777@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Sizning Verified Kodingiz...");
        message.setText("Assalomu alekum siznig verified kodingiz : " + body);
        mailSender.send(message);
    }

    public String randomCode(){
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
