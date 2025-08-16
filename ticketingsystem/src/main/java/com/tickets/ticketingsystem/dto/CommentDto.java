package com.tickets.ticketingsystem.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime createdAt;
}
