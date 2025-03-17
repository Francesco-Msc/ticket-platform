package org.milestone.platform.ticket_platform.controller;

import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

        boolean isAdmin = false;
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("Admin")) {
                isAdmin = true;
                break;
            }
        }
        if (isAdmin) {
            model.addAttribute("tickets", ticketService.findAll());
        }
        model.addAttribute("tickets", ticketService.getTicketsAssignedToUser(loggedUser));
        return "dashboard/index";
    }

    @GetMapping("/search")
    public String findByKeyword(@RequestParam(name = "query") String query, Model model) {
        model.addAttribute("tickets", ticketService.findByQuery(query));
        return "dashboard/index";
    }

}
