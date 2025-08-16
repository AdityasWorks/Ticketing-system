package com.tickets.ticketingsystem.service;

import java.util.List;

import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.model.TicketStatus;

public interface TicketService {
    TicketDto createTicket(CreateTicketDto createTicketDto, String username);
    List<TicketDto> getTicketsForUser(String username);

    List<TicketDto> getAllTickets();
    TicketDto getTicketById(Long id);
    TicketDto assignTicket(Long ticketId, String username);

    TicketDto forceReassignTicket(Long ticketId, Long assigneeId);
    TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus);
    

}
