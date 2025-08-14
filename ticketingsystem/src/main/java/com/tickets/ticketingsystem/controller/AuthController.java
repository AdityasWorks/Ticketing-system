package com.tickets.ticketingsystem.controller;

import com.tickets.ticketingsystem.dto.JwtAuthenticationResponseDto;
import com.tickets.ticketingsystem.dto.LoginRequestDto;
import com.tickets.ticketingsystem.dto.UserRegistrationDto;
import com.tickets.ticketingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            userService.registerNewUser(registrationDto);
            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        String token = userService.authenticate(loginRequestDto);
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(token));
    }

    
}