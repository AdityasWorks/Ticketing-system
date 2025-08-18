package com.tickets.ticketingsystem.dto;

import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;
    private String fileName;
    private String fileUrl;
}
