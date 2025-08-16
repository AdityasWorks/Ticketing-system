package com.tickets.ticketingsystem.service.impl;

import com.tickets.ticketingsystem.dto.CommentDto;
import com.tickets.ticketingsystem.dto.CreateCommentDto;
import com.tickets.ticketingsystem.model.Comment;
import com.tickets.ticketingsystem.model.Ticket;
import com.tickets.ticketingsystem.model.User;
import com.tickets.ticketingsystem.repository.CommentRepository;
import com.tickets.ticketingsystem.repository.TicketRepository;
import com.tickets.ticketingsystem.repository.UserRepository;
import com.tickets.ticketingsystem.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long ticketId, CreateCommentDto commentDto, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));
        User author = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setTicket(ticket);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        return convertCommentToDto(savedComment);
    }

    private CommentDto convertCommentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}