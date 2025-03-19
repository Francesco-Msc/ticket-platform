package org.milestone.platform.ticket_platform.repository;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.milestone.platform.ticket_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
    public List<Ticket> findByTitleContaining(String query);

    public List<Ticket> findByUserId(Integer userId);

    public List<Ticket> findByTitleContainingAndUser(String title, User user);
}
