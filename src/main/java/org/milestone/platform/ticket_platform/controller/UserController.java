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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("openTickets", userService.getOpenTicketsByUser(userService.getById(id)));
        return "users/operatorDetails";
    }

    @GetMapping("/personal-area")
    public String personalArea(Model model){
        model.addAttribute("isAvailable", userService.getCurrentUser().getIsAvailable());
        model.addAttribute("openTickets", userService.getOpenTicketsByUser(userService.getCurrentUser()));
        model.addAttribute("user", userService.getCurrentUser());
        return "users/personal-area";
    }

    @PostMapping("/updateUserStatus")
    public String updateUserStatus(@RequestParam("isAvailable") Boolean isAvailable, Model model){
        User currentUser = userService.getCurrentUser();
        if (ticketService.isTicketCompleted(currentUser)) {
            currentUser.setIsAvailable(isAvailable);
            userService.update(currentUser);
        } else {
            model.addAttribute("errorMessage", "Cannot go offline if there are still open tickets");
            model.addAttribute("isAvailable", currentUser.getIsAvailable());
            model.addAttribute("openTickets", userService.getOpenTicketsByUser(userService.getCurrentUser()));
            model.addAttribute("user", userService.getCurrentUser());
            return "users/personal-area";
        }
        return "redirect:/users/personal-area";
    }

    @PostMapping("/updateUser")
    public String updateUser(@RequestParam(required = false) String newUsername, 
                             @RequestParam(required = false) String newPassword,
                             @RequestParam(required = false) String confirmPassword,
                             RedirectAttributes redirectAttributes,
                             Model model){

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            if (newUsername != null && !newUsername.trim().isEmpty()) {
                currentUser.setUsername(newUsername.trim());
            }
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (newPassword.equals(confirmPassword)) {
                    userService.updatePassword(currentUser, newPassword.trim());
                } else {
                    redirectAttributes.addFlashAttribute("errorPasswordMismatch", "Passwords do not match!");
                    return "redirect:/users/personal-area";
                }
            }
            userService.update(currentUser);
            model.addAttribute("user", currentUser);
            
            userService.updateAuthentication(currentUser);
        }
        return "redirect:/users/personal-area";
    }
}
