package org.milestone.platform.ticket_platform.repository;

import org.milestone.platform.ticket_platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
