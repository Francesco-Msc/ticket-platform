package org.milestone.platform.ticket_platform.repository;

import org.milestone.platform.ticket_platform.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    
}
