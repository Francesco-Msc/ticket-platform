package org.milestone.platform.ticket_platform.service;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    
    @Autowired
    private TicketRepository ticketRepo;
    
    public List<Ticket> findAll(){
        return ticketRepo.findAll();
    }
}
