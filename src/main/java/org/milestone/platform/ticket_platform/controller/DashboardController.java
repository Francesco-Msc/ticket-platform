package org.milestone.platform.ticket_platform.controller;

import org.milestone.platform.ticket_platform.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/search")
    public String findByKeyword(@RequestParam(name = "query") String query, Model model){
        model.addAttribute("tickets", ticketService.findByQuery(query));
        return "dashboard/index";
    }

    
}
