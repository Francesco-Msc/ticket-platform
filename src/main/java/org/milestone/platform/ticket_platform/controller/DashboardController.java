package org.milestone.platform.ticket_platform.controller;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, Authentication authentication) {
        User loggedUser = userService.getCurrentUser();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Admin"))) {
            List<Ticket> tickets = ticketService.findAll();
            model.addAttribute("tickets", tickets);
            model.addAttribute("isAdmin", true);
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Operator"))) {
            List<Ticket> tickets = ticketService.getTicketsAssignedToUser(loggedUser);
            model.addAttribute("tickets", tickets);
            model.addAttribute("isAdmin", false);
        }
        return "dashboard/index";
    }

    @GetMapping("/search")
    public String findByKeyword(@RequestParam(name = "query") String query, Model model, Authentication authentication) {
        User loggedUser = userService.getCurrentUser();
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("Admin"))) {
            model.addAttribute("tickets", ticketService.findByQuery(query));
        } else {
            model.addAttribute("tickets", ticketService.findByQueryAndUser(query, loggedUser));
        }
        return "dashboard/index";
    }

}
