package org.milestone.platform.ticket_platform.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.milestone.platform.ticket_platform.enums.TicketStatus;
import org.milestone.platform.ticket_platform.model.Note;
import org.milestone.platform.ticket_platform.model.Role;
import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.repository.NoteRepository;
import org.milestone.platform.ticket_platform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private NoteRepository noteRepo;

    @Autowired
    private UserService userService;
    
    public List<Ticket> findAll(){
        return ticketRepo.findAll();
    }

    public Ticket getById(Integer id){
        return ticketRepo.findById(id).get();
    }

    public List<Ticket> findByQuery(String query){
        return ticketRepo.findByTitleContaining(query);
    }

    public List<Ticket> findByQueryAndUser(String query, User user) {
        return ticketRepo.findByTitleContainingAndUser(query, user);
    }

    // Crea un ticket, inizializza una lista di note vuota se non esistono note, e se il campo note è popolato aggiunge le note alla lista.
    public void create(Ticket addTicket){
        if (addTicket.getNotes() != null) {
            List<Note> notEmptyNotes = new ArrayList<>();
            for (Note note : addTicket.getNotes()) {
                if (note.getText() != null && !note.getText().trim().isEmpty()) {
                    notEmptyNotes.add(note);
                }
            }
            addTicket.setNotes(notEmptyNotes);
        }
        ticketRepo.save(addTicket);
        if (addTicket.getNotes() != null && !addTicket.getNotes().isEmpty()) {
            for (Note note : addTicket.getNotes()) {
                noteRepo.save(note);
            }
        }
    }

    public Ticket update(Ticket updaTicket){
        return ticketRepo.save(updaTicket);
    }

    // Elimina un ticket e tutte le sie note
    public void delete(Ticket ticket){
        for (Note note : ticket.getNotes()) {
            noteRepo.delete(note);
        }
        ticketRepo.delete(ticket);
    }

    public List<Note> getNotesByTicketId(Integer ticketId){
        return noteRepo.findByTicketId(ticketId);
    }

    public List<Ticket> getTicketsAssignedToUser(User user) {
        return ticketRepo.findByUserId(user.getId());
    }

    public void updateStatus(Integer id, TicketStatus newStatus){
        Ticket ticket = ticketRepo.findById(id).get();
        ticket.setStatus(newStatus);
        ticket.setUpdated_at(LocalDateTime.now());
        ticketRepo.save(ticket);
    }

    public Boolean isTicketCompleted(User user){
        List<Ticket> tickets = ticketRepo.findByUserId(user.getId());
        if (tickets.isEmpty()) {
            return true;
        }
        for (Ticket ticket : tickets) {
            if (!ticket.getStatus().equals(TicketStatus.COMPLETED)) {
                return false;
            }
        }
        return true;
    }

    // Verifica se il ticket è assegnato all'utente loggato o se l'utente è un admin altrimenti nega l'accesso
    public boolean isTicketAssigned(Integer ticketId){
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
            for (Role role : currentUser.getRoles()) {
                if (role.getName().equals("Admin")) {
                    return true;
                }
            }
        }
        List<Ticket> assignedTickets = ticketRepo.findByUserId(currentUser.getId());
        for (Ticket ticket : assignedTickets) {
            if (ticket.getId().equals(ticketId)) {
                return true;
            }
        }
        throw new AccessDeniedException("Access denied");
    }

    public List<Ticket> findByCategory(String category){
        return ticketRepo.findByCategoryNameContaining(category);
    }
}
