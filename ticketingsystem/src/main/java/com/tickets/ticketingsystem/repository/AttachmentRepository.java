package com.tickets.ticketingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickets.ticketingsystem.model.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {


}
