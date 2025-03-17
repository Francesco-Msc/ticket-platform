package org.milestone.platform.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.milestone.platform.ticket_platform.enums.TicketStatus;
import org.milestone.platform.ticket_platform.model.Note;
import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.service.CategoryService;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Integer id, Model model){
        model.addAttribute("tickets", ticketService.getById(id));
        model.addAttribute("isDetail", true);
        model.addAttribute("notes", ticketService.getNotesByTicketId(id));
        return "dashboard/show";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("create", true);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.availableOperators(userService.findAll()));
        return "dashboard/create-edit";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("ticket") Ticket addTicket, BindingResult bindingResult,  Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.availableOperators(userService.findAll()));
            return "dashboard/create-edit";
        }

        if (addTicket.getNotes() != null) {
            for (Note note : addTicket.getNotes()) {
                note.setTicket(addTicket);
                note.setUser(userService.getById(1));                           //NEED TO BE FIXED USER ID
            }
        }

        ticketService.create(addTicket);
        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("ticket", ticketService.getById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.availableOperators(userService.findAll()));
        model.addAttribute("stauses", TicketStatus.values());
        return "dashboard/create-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket updaTicket, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.availableOperators(userService.findAll()));
            model.addAttribute("stauses", TicketStatus.values());
            return "dashboard/create-edit";
        }

        Ticket existingTicket = ticketService.getById(id);
        updaTicket.setUpdated_at(LocalDateTime.now());
        updaTicket.setCreation_date(existingTicket.getCreation_date());
        model.addAttribute("stauses", TicketStatus.values());

        ticketService.update(updaTicket);
        return "redirect:/ticket/" + id;
    }

    @GetMapping("/note/{id}")
    public String note(@PathVariable("id") Integer id, Model model){
        Note note = new Note();
        note.setTicket(ticketService.getById(id));
        model.addAttribute("note", note);
        return "notes/create-note";
    }

    @GetMapping("/{id}/notes")
    public String showNotes(@PathVariable("id") Integer id, Model model) {
        Ticket ticket = ticketService.getById(id);
        
        if (ticket == null) {
            model.addAttribute("error", "Ticket non trovato");
            return "error";
        }

        List<Note> notes = ticketService.getNotesByTicketId(id);

        model.addAttribute("ticket", ticket);
        model.addAttribute("notes", notes);
        model.addAttribute("isNotes", true);

        return "notes/show-notes";
    }

}
