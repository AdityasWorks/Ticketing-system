package com.tickets.ticketingsystem.dto;

import com.tickets.ticketingsystem.model.Role;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
}
