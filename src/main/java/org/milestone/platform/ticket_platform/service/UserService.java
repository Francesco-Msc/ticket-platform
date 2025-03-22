package org.milestone.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.milestone.platform.ticket_platform.enums.TicketStatus;
import org.milestone.platform.ticket_platform.model.Role;
import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.milestone.platform.ticket_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    private UserRepository userRepo;

    // Mostra la lista di utenti escludendo quelli con ruolo admin
    public List<User> findAll() {
        List<User> users = userRepo.findAll();
        List<User> filteredUsers = new ArrayList<>();

        for (User user : users) {
            boolean isAdmin = false;
            for (Role role : user.getRoles()) {
                if (role.getName().equals("Admin")) {
                    isAdmin = true;
                    break;
                }
            }
            if (!isAdmin) {
                filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    public List<User> availableOperators(List<User> users) {
        List<User> availableOp = new ArrayList<>();
        for (User user : users) {
            if (user.isAvailable()) {
                availableOp.add(user);
            }
        }
        return availableOp;
    }

    public User getById(Integer id) {
        return userRepo.findById(id).get();
    }

    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // Restituisce l'utente attualmente autenticato basato sul nome utente
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void update(User currentUser) {
        userRepo.save(currentUser);
    }

    // Restituisce i ticket NON completati assegnati all'utente
    public List<Ticket> getOpenTicketsByUser(User user) {
        List<Ticket> userOpenTickets = new ArrayList<>();
        for (Ticket ticket : user.getTickets()) {
            if (!ticket.getStatus().equals(TicketStatus.COMPLETED)) {
                userOpenTickets.add(ticket);
            }
        }
        return userOpenTickets;
    }

    // Aggiorna l'autenticazione dell'utente con i nuovi dati cosi che al cambio di username o password l'utente rimane loggato
    public void updateAuthentication(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                authentication.getCredentials(),
                authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void updatePassword(User user, String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepo.save(user);
        }
    }
}
