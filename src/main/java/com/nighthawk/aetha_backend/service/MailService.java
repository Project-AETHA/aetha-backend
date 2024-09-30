package com.nighthawk.aetha_backend.service;

import com.nighthawk.aetha_backend.dto.MailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(MailDTO mail) {
        try {
            // Send mail - basic template
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(mail.getTo());
            message.setSubject(mail.getSubject());
            message.setText(mail.getMessage());
            message.setFrom("2021cs067@stu.ucsc.cmb.ac.lk");
            mailSender.send(message);

            System.out.printf("Email send to %s successfully%n", mail.getTo());
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }

}
