package com.tickets.ticketingsystem.service;

import java.util.List;

import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;

public interface TicketService {
    TicketDto createTicket(CreateTicketDto createTicketDto, String username);
    List<TicketDto> getTicketsForUser(String username);

}
