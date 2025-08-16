package com.tickets.ticketingsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickets.ticketingsystem.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{

    List<Comment> findByTicketIdOrderByCreatedAtAsc(Long tickedId);

}
