package com.tickets.ticketingsystem.service;

public interface EmailService {
    void sendSimpleMailMessage(String to, String subject, String text);

}
