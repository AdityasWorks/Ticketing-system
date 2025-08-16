package com.tickets.ticketingsystem.dto;

import com.tickets.ticketingsystem.model.Priority;

import lombok.Data;

@Data
public class CreateTicketDto {
    private String subject;
    private String description;
    private Priority priority;
}
