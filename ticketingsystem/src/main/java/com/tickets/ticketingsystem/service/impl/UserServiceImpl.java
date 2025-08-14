package com.tickets.ticketingsystem.service.impl;

import com.tickets.ticketingsystem.dto.UserRegistrationDto;
import com.tickets.ticketingsystem.model.Role;
import com.tickets.ticketingsystem.model.User;
import com.tickets.ticketingsystem.repository.UserRepository;
import com.tickets.ticketingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerNewUser(UserRegistrationDto registrationDto){
        // Check if user already exists
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email " + registrationDto.getEmail() + " already exists.");
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

}
