package com.tickets.ticketingsystem.service;

import java.util.List;

import com.tickets.ticketingsystem.dto.LoginRequestDto;
import com.tickets.ticketingsystem.dto.UserDto;
import com.tickets.ticketingsystem.dto.UserRegistrationDto;
import com.tickets.ticketingsystem.model.Role;
import com.tickets.ticketingsystem.model.User;

public interface UserService {

    User registerNewUser(UserRegistrationDto registrationDto);
    String authenticate(LoginRequestDto loginRequestDto);

    List<UserDto> getAllUsers();
    User updateUserRole(Long userId, Role newRole);


}
