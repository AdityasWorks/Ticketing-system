package com.tickets.ticketingsystem.dto;

import com.tickets.ticketingsystem.model.Role;

import lombok.Data;

@Data
public class AdminCreateUserDto {
    private String name;
    private String email;
    private String password;
    private Role role;
}


