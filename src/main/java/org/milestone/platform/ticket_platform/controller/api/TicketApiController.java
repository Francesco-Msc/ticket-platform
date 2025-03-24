package org.milestone.platform.ticket_platform.controller.api;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketApiController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<Ticket> index(){
        return ticketService.findAll();
    }
    
}
