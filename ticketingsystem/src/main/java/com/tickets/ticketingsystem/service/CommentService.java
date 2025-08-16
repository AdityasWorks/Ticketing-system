package com.tickets.ticketingsystem.service;

import com.tickets.ticketingsystem.dto.CommentDto;
import com.tickets.ticketingsystem.dto.CreateCommentDto;

public interface CommentService {
    CommentDto createComment(Long ticketId, CreateCommentDto commentDto, String username);

}
