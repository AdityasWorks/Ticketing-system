package com.tickets.ticketingsystem.controller;

import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody CreateTicketDto createTicketDto, Principal principal) {
        // principal.getName() will give us the email of the currently logged-in user
        TicketDto createdTicket = ticketService.createTicket(createTicketDto, principal.getName());
        return ResponseEntity.ok(createdTicket);
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getUserTickets(Principal principal) {
        List<TicketDto> tickets = ticketService.getTicketsForUser(principal.getName());
        return ResponseEntity.ok(tickets);
    }
}