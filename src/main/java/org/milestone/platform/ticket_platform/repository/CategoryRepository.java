package org.milestone.platform.ticket_platform.repository;

import org.milestone.platform.ticket_platform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
   
}