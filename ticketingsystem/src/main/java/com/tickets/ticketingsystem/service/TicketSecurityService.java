package com.tickets.ticketingsystem.service;

import com.tickets.ticketingsystem.model.Ticket;
import com.tickets.ticketingsystem.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("ticketSecurityService")
public class TicketSecurityService {

    @Autowired
    private TicketRepository ticketRepository;

    public boolean isOwnerOrAssignee(Long ticketId, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        boolean isOwner = ticket.getRequester().getEmail().equals(username);
        boolean isAssignee = ticket.getAssignee() != null && ticket.getAssignee().getEmail().equals(username);

        return isOwner || isAssignee;
    }

    public boolean isAssignee(Long ticketId, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        return ticket.getAssignee() != null && ticket.getAssignee().getEmail().equals(username);
    }
}