package org.milestone.platform.ticket_platform.controller;

import org.milestone.platform.ticket_platform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TicketService ticketService;
    
    @GetMapping
    public String index(Model model){
        model.addAttribute("tickets", ticketService.findAll());
        return "dashboard/index";
    }

    @GetMapping("/ticket/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        model.addAttribute("tickets", ticketService.getById(id));
        return "dashboard/show";
    }
}
