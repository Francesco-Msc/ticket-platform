package org.milestone.platform.ticket_platform.controller;

import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String index(Model model){
        model.addAttribute("users", userService.findAll());
        return "users/operators";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("ticekts", userService.getTicketsByUserId(id));
        return "users/operatorDetails";
    }

    @GetMapping("/personal-area")
    public String personalArea(Model model){
        model.addAttribute("isAvailable", userService.getCurrentUser().getIsAvailable());
        return "users/personal-area";
    }

    @PostMapping("/updateUserStatus")
    public String updateUserStatus(@RequestParam("isAvailable") Boolean isAvailable, Model model){
        User currentUser = userService.getCurrentUser();
        if (ticketService.isTicketCompleted(currentUser)) {
            currentUser.setIsAvailable(isAvailable);
            userService.update(currentUser);
            return "redirect:/users/personal-area";
        } 
        model.addAttribute("error", "Cannot go offline if there are still open tickets");
        return "error";
    }
}
