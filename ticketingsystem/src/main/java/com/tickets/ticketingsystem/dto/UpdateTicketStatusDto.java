package com.tickets.ticketingsystem.dto;

import com.tickets.ticketingsystem.model.TicketStatus;

import lombok.Data;

@Data
public class UpdateTicketStatusDto {
    private TicketStatus status;
}
