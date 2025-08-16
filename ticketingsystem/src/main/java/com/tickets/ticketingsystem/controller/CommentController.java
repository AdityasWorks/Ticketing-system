package com.tickets.ticketingsystem.controller;

import com.tickets.ticketingsystem.dto.CommentDto;
import com.tickets.ticketingsystem.dto.CreateCommentDto;
import com.tickets.ticketingsystem.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/tickets/{ticketId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    // --- FIX IS ON THIS LINE ---
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPPORT_AGENT') or @ticketSecurityService.isOwnerOrAssignee(#ticketId, authentication.name)")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long ticketId,
                                               @RequestBody CreateCommentDto commentDto,
                                               Principal principal) {
        CommentDto newComment = commentService.createComment(ticketId, commentDto, principal.getName());
        return ResponseEntity.ok(newComment);
    }
}