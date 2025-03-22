package org.milestone.platform.ticket_platform.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.milestone.platform.ticket_platform.enums.TicketStatus;
import org.milestone.platform.ticket_platform.model.Note;
import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.service.CategoryService;
import org.milestone.platform.ticket_platform.service.TicketService;
import org.milestone.platform.ticket_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        ticketService.isTicketAssigned(id);
        model.addAttribute("tickets", ticketService.getById(id));
        model.addAttribute("isDetail", true);
        model.addAttribute("notes", ticketService.getNotesByTicketId(id));
        model.addAttribute("isAvailable", userService.getCurrentUser().getIsAvailable());
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
    public String store(@Valid @ModelAttribute("ticket") Ticket addTicket, BindingResult bindingResult, Authentication authentication,  Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.availableOperators(userService.findAll()));
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong, please try again, if the error persist contact the support.");
            return "dashboard/create-edit";
        }
        User loggedUser = userService.getCurrentUser();

        if (addTicket.getNotes() != null) {
            for (Note note : addTicket.getNotes()) {
                note.setTicket(addTicket);
                note.setUser(loggedUser);
            }
        }
        ticketService.create(addTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket added successfully!");
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
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket updaTicket, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.availableOperators(userService.findAll()));
            model.addAttribute("stauses", TicketStatus.values());
            redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong, please try again, if the error persist contact the support.");
            return "dashboard/create-edit";
        }

        Ticket existingTicket = ticketService.getById(id);
        updaTicket.setUpdated_at(LocalDateTime.now());
        updaTicket.setCreation_date(existingTicket.getCreation_date());
        model.addAttribute("stauses", TicketStatus.values());

        ticketService.update(updaTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket updated successfully!");
        return "redirect:/ticket/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes){
        Ticket ticket = ticketService.getById(id);
        if (!ticket.getStatus().equals(TicketStatus.COMPLETED)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket status must be COMPLETED to be deleted.");
            return "redirect:/dashboard";
        }
        try {
            ticketService.delete(ticket);
            redirectAttributes.addFlashAttribute("successMessage", "Ticket deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting the ticket.");
        }

        return "redirect:/dashboard";
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
        ticketService.isTicketAssigned(id);
        
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

    @GetMapping("/{id}/status")
    public String changeStatus(@PathVariable("id") Integer id, Model model){
        ticketService.isTicketAssigned(id);
        model.addAttribute("ticket", ticketService.getById(id));
        model.addAttribute("stauses", TicketStatus.values());
        return "tickets/change-status";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable("id") Integer id, @RequestParam("status") TicketStatus newStatus, Model model, RedirectAttributes redirectAttributes){
        ticketService.isTicketAssigned(id);
        ticketService.updateStatus(id, newStatus);
        redirectAttributes.addFlashAttribute("successMessage", "Status updated successfully!");
        return "redirect:/dashboard";
    }
}
