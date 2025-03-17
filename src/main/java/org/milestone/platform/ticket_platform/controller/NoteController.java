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
import jakarta.validation.Valid;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("note") Note addNote, BindingResult bindingResult, Model model, Authentication authentication){
        if (bindingResult.hasErrors()) {
            return "notes/create-note";
        }

        String username = authentication.getName();
        User loggedUser = userService.findByUsername(username)
                                   .orElseThrow(() -> new RuntimeException("User not found"));
        
        addNote.setUser(loggedUser);
        addNote.setCreatedAt(LocalDateTime.now());
        
        noteService.create(addNote);
        return "redirect:/dashboard"; 
    }
}
