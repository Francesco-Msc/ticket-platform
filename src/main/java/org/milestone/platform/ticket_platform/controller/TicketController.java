package org.milestone.platform.ticket_platform.controller;

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

        ticketService.create(addTicket);
        return "redirect:/dashboard";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        model.addAttribute("ticket", ticketService.getById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("users", userService.availableOperators(userService.findAll()));
        return "dashboard/create-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("ticket") Ticket updaTicket, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("users", userService.availableOperators(userService.findAll()));
            return "dashboard/create-edit";
        }
        Ticket existingTicket = ticketService.getById(id);
        updaTicket.setCreation_date(existingTicket.getCreation_date());
        ticketService.update(updaTicket);
        return "redirect:/ticket/" + id;
    }
}
