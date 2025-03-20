package org.milestone.platform.ticket_platform.controller;


import java.time.LocalDateTime;

import org.milestone.platform.ticket_platform.model.Note;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.service.NoteService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("note") Note addNote, BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong, please try again, if the error persist contact the support.");
            return "notes/create-note";
        }
        User loggedUser = userService.getCurrentUser();
        
        addNote.setUser(loggedUser);
        addNote.setCreatedAt(LocalDateTime.now());

        noteService.create(addNote);
        Integer ticketId = addNote.getTicket().getId();
        redirectAttributes.addFlashAttribute("successMessage", "Note added successfully!");
        return "redirect:/ticket/" + ticketId + "/notes"; 
    }
}
