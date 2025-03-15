package org.milestone.platform.ticket_platform.controller;

import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
}
