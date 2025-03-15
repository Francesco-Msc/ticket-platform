package org.milestone.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.repository.TicketRepository;
import org.milestone.platform.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
