package org.milestone.platform.ticket_platform.repository;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByTitleContains(String query);
}
