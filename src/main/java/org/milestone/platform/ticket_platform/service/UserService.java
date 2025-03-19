package org.milestone.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.milestone.platform.ticket_platform.enums.TicketStatus;
import org.milestone.platform.ticket_platform.model.Role;
import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.repository.TicketRepository;
import org.milestone.platform.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TicketRepository ticketRepo;

    public List<User> findAll(){
        List<User> users = userRepo.findAll();
        List<User> filteredUsers = new ArrayList<>();
        
        for (User user : users) {
            boolean isAdmin = false;
            for (Role role : user.getRoles()) {
                if (role.getName().equals("Admin")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    public List<Ticket> getTicketsByUserId(Integer userId) {
        return ticketRepo.findByUserId(userId);
    }

    public List<User> availableOperators(List<User> users) {
        List<User> availableOp = new ArrayList<>();
        for (User user : users) {
            if (user.isAvailable()) {
                availableOp.add(user);
            }
        }
        return availableOp;
    }

    public User getById(Integer id){
        return userRepo.findById(id).get();
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void update(User currentUser){
        userRepo.save(currentUser);
    }

    public List<Ticket> getOpenTicketsByUser(User user){
        List<Ticket> userOpenTickets = new ArrayList<>();
        for (Ticket ticket : user.getTickets()) {
            if (!ticket.getStatus().equals(TicketStatus.COMPLETED)) {
                userOpenTickets.add(ticket);
            }
        }
        return userOpenTickets;
    }
}
