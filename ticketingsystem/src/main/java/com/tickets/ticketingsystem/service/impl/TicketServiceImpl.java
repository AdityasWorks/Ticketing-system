package com.tickets.ticketingsystem.service.impl;

import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.model.Ticket;
import com.tickets.ticketingsystem.model.TicketStatus;
import com.tickets.ticketingsystem.model.User;
import com.tickets.ticketingsystem.repository.TicketRepository;
import com.tickets.ticketingsystem.repository.UserRepository;
import com.tickets.ticketingsystem.service.TicketService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public TicketDto createTicket(CreateTicketDto createTicketDto, String username) {
        User requester = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        Ticket ticket = new Ticket();
        ticket.setSubject(createTicketDto.getSubject());
        ticket.setDescription(createTicketDto.getDescription());
        ticket.setPriority(createTicketDto.getPriority());
        ticket.setStatus(TicketStatus.OPEN); // Default status
        ticket.setRequester(requester);

        Ticket savedTicket = ticketRepository.save(ticket);
        return convertToDto(savedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsForUser(String username) {
        User requester = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return ticketRepository.findByRequesterId(requester.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Entity to DTO
    private TicketDto convertToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setRequesterName(ticket.getRequester().getName());
        if (ticket.getAssignee() != null) {
            dto.setAssigneeName(ticket.getAssignee().getName());
        }
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        return dto;
    }
}