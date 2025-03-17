package org.milestone.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        return userRepo.findAll();
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
}
