package com.tickets.ticketingsystem.service;

import com.tickets.ticketingsystem.dto.UserRegistrationDto;
import com.tickets.ticketingsystem.model.User;

public interface UserService {

    User registerNewUser(UserRegistrationDto registrationDto);

}
