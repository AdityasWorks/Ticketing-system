package com.tickets.ticketingsystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.tickets.ticketingsystem.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMailMessage(String to , String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@ticketingsystem.com"); // This can be any address
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
