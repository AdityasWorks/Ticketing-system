package com.tickets.ticketingsystem.dto;

import java.time.LocalDateTime;

import com.tickets.ticketingsystem.model.Priority;
import com.tickets.ticketingsystem.model.TicketStatus;

import lombok.Data;

@Data
public class TicketDto {
    private Long id;
    private String subject;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private String requesterName;
    private String assigneeName; // Can be null
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
