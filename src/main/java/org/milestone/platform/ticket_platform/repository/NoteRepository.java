package org.milestone.platform.ticket_platform.repository;

import org.milestone.platform.ticket_platform.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Integer> {

}
