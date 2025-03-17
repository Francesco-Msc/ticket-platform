package org.milestone.platform.ticket_platform.service;

import org.milestone.platform.ticket_platform.model.Note;
import org.milestone.platform.ticket_platform.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepo;

    public Note create(Note addNote){
        return noteRepo.save(addNote);
    }

    public Note getById(Integer id){
        return noteRepo.findById(id).get();
    }
}
