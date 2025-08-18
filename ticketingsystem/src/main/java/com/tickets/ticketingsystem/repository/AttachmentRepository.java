package com.tickets.ticketingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickets.ticketingsystem.model.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

        List<Attachment> findByTicketId(Long ticketId);


}
