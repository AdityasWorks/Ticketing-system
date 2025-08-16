package com.tickets.ticketingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickets.ticketingsystem.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {

    List<Ticket> findByRequesterId(Long requesterId);

    
}
