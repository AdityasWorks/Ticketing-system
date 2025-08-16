package com.tickets.ticketingsystem.controller;

import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.dto.UpdateTicketStatusDto;
import com.tickets.ticketingsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT_AGENT')")

    public ResponseEntity<List<TicketDto>> getAllTickets(){
        List<TicketDto> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT_AGENT')")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id){
        TicketDto ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPPORT_AGENT')")
    public ResponseEntity<TicketDto> assignTicket(@PathVariable Long id, Principal principal){
        TicketDto assignedTicket = ticketService.assignTicket(id, principal.getName());
        return ResponseEntity.ok(assignedTicket);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SUPPORT_AGENT') and @ticketSecurityService.isAssignee(#id, authentication.name)")
    public ResponseEntity<TicketDto> updateTicketStatusByAgent(@PathVariable Long id, 
                                                             @RequestBody UpdateTicketStatusDto dto, 
                                                             Principal principal) {
        TicketDto updatedTicket = ticketService.updateTicketStatusByAgent(id, dto.getStatus(), principal.getName());
        return ResponseEntity.ok(updatedTicket);
    }
}