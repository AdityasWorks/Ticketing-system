package com.tickets.ticketingsystem.service.impl;

import com.tickets.ticketingsystem.dto.AttachmentDto;
import com.tickets.ticketingsystem.dto.CommentDto;
import com.tickets.ticketingsystem.dto.CreateTicketDto;
import com.tickets.ticketingsystem.dto.TicketDto;
import com.tickets.ticketingsystem.model.Attachment;
import com.tickets.ticketingsystem.model.Role;
import com.tickets.ticketingsystem.model.Ticket;
import com.tickets.ticketingsystem.model.TicketStatus;
import com.tickets.ticketingsystem.model.User;
import com.tickets.ticketingsystem.repository.AttachmentRepository;
import com.tickets.ticketingsystem.repository.CommentRepository;
import com.tickets.ticketingsystem.repository.TicketRepository;
import com.tickets.ticketingsystem.repository.UserRepository;
import com.tickets.ticketingsystem.service.EmailService;
import com.tickets.ticketingsystem.service.FileStorageService;
import com.tickets.ticketingsystem.service.TicketService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired(required = false)
    private FileStorageService fileStorageService;

    @Autowired
    private AttachmentRepository attachmentRepository;



    @Override
    @Transactional
    public TicketDto createTicket(CreateTicketDto createTicketDto, String username, MultipartFile file) {
        User requester = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        Ticket ticket = new Ticket();
        ticket.setSubject(createTicketDto.getSubject());
        ticket.setDescription(createTicketDto.getDescription());
        ticket.setPriority(createTicketDto.getPriority());
        ticket.setStatus(TicketStatus.OPEN); // Default status
        ticket.setRequester(requester);

        Ticket savedTicket = ticketRepository.save(ticket);
        
        //file upload

        if (fileStorageService != null && file != null && !file.isEmpty()) {
            String fileUrl = fileStorageService.uploadFile(file);
            Attachment attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFileUrl(fileUrl);
            attachment.setTicket(savedTicket);
            attachmentRepository.save(attachment);
        }



        // email
        String emailSubject = String.format("Ticket #%d Created: %s", savedTicket.getId(), savedTicket.getSubject());
        String emailText = String.format("Hello %s,\n\nYour support ticket has been successfully created.\n\nTicket ID: %d\nSubject: %s\nPriority: %s\n\nWe will get back to you shortly.\n\nThe Ticketing System Team",
            requester.getName(),
            savedTicket.getId(),
            savedTicket.getSubject(),
            savedTicket.getPriority()
        );
        emailService.sendSimpleMailMessage(requester.getEmail(), emailSubject, emailText);

        
        return convertToDto(savedTicket);
    }

    

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getTicketsForUser(String username) {
        User requester = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return ticketRepository.findByRequesterId(requester.getId())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDto> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TicketDto getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + id));
        return convertToDto(ticket);
    }

    @Override
    @Transactional
    public TicketDto assignTicket(Long ticketId, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        User assignee = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        ticket.setAssignee(assignee);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDto(updatedTicket);
    }


    // Helper method to convert Entity to DTO
    private TicketDto convertToDto(Ticket ticket) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setSubject(ticket.getSubject());
        dto.setDescription(ticket.getDescription());
        dto.setStatus(ticket.getStatus());
        dto.setPriority(ticket.getPriority());
        dto.setRequesterName(ticket.getRequester().getName());
        if (ticket.getAssignee() != null) {
            dto.setAssigneeName(ticket.getAssignee().getName());
        }
        dto.setCreatedAt(ticket.getCreatedAt());
        dto.setUpdatedAt(ticket.getUpdatedAt());
        

        List<CommentDto> comments = commentRepository.findByTicketIdOrderByCreatedAtAsc(ticket.getId())
            .stream()
            .map(comment -> {
                CommentDto commentDto = new CommentDto();
                commentDto.setId(comment.getId());
                commentDto.setText(comment.getText());
                commentDto.setAuthorName(comment.getAuthor().getName());
                commentDto.setCreatedAt(comment.getCreatedAt());
                return commentDto;
            })
            .collect(Collectors.toList());
        dto.setComments(comments);


        // Attachments
        List<AttachmentDto> attachmentDtos = attachmentRepository.findByTicketId(ticket.getId())
                .stream()
                .map(attachment -> {
                    AttachmentDto attachmentDto = new AttachmentDto();
                    attachmentDto.setId(attachment.getId());
                    attachmentDto.setFileName(attachment.getFileName());
                    attachmentDto.setFileUrl(attachment.getFileUrl());
                    return attachmentDto;
                })
                .collect(Collectors.toList());
        dto.setAttachments(attachmentDtos);

        return dto;
    }

    @Override
    @Transactional
    public TicketDto forceReassignTicket(Long ticketId, Long assigneeId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("Assignee user not found with id: " + assigneeId));

        // Optional: Check if the assignee is a support agent or admin
        if (assignee.getRole() != Role.SUPPORT_AGENT && assignee.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Tickets can only be assigned to ADMIN or SUPPORT_AGENT roles.");
        }

        ticket.setAssignee(assignee);
        // If the ticket was open, move it to in progress.
        if (ticket.getStatus() == TicketStatus.OPEN) {
            ticket.setStatus(TicketStatus.IN_PROGRESS);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDto(updatedTicket);
    }

    @Override
    @Transactional
    public TicketDto updateTicketStatus(Long ticketId, TicketStatus newStatus) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        ticket.setStatus(newStatus);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDto(updatedTicket);
    }

    @Override
    @Transactional
    public TicketDto updateTicketStatusByAgent(Long ticketId, TicketStatus newStatus, String agentUsername) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found with id: " + ticketId));

        User agent = userRepository.findByEmail(agentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + agentUsername));

        if (ticket.getAssignee() == null || !ticket.getAssignee().getId().equals(agent.getId())) {
            throw new IllegalStateException("You are not assigned to this ticket.");
        }

        ticket.setStatus(newStatus);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return convertToDto(updatedTicket);
    }
    
}