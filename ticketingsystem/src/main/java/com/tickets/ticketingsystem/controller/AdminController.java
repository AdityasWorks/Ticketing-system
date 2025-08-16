package com.tickets.ticketingsystem.controller;

import com.tickets.ticketingsystem.dto.AdminCreateUserDto;
import com.tickets.ticketingsystem.dto.ReassignTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.dto.UpdateRoleDto;
import com.tickets.ticketingsystem.dto.UpdateTicketStatusDto;
import com.tickets.ticketingsystem.dto.UserDto;
import com.tickets.ticketingsystem.service.TicketService;
import com.tickets.ticketingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody UpdateRoleDto dto) {
        try {
            userService.updateUserRole(id, dto.getRole());
            return ResponseEntity.ok("User role updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/tickets/{id}/reassign")
    public ResponseEntity<TicketDto> reassignTicket(@PathVariable Long id, @RequestBody ReassignTicketDto dto) {
        TicketDto updatedTicket = ticketService.forceReassignTicket(id, dto.getAssigneeId());
        return ResponseEntity.ok(updatedTicket);
    }

    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<TicketDto> updateTicketStatus(@PathVariable Long id, @RequestBody UpdateTicketStatusDto dto) {
        TicketDto updatedTicket = ticketService.updateTicketStatus(id, dto.getStatus());
        return ResponseEntity.ok(updatedTicket);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> addUser(@RequestBody AdminCreateUserDto userDto) {
        UserDto newUser = userService.addUser(userDto);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id) {
        try {
            userService.removeUser(id);
            return ResponseEntity.ok("User removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}