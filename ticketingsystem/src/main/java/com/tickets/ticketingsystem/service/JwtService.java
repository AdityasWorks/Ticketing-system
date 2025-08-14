package com.tickets.ticketingsystem.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    
    String extractUsername(String token);
    String generateToken(UserDetails UserDetails);
    boolean isTokenValid(String token, UserDetails userDetails);

}
