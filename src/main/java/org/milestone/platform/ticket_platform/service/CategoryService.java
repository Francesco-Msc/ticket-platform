package org.milestone.platform.ticket_platform.service;

import java.util.List;

import org.milestone.platform.ticket_platform.model.Category;
import org.milestone.platform.ticket_platform.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;

    public List<Category> findAll(){
        return categoryRepo.findAll();
    }

    public Category getById(Integer id){
        return categoryRepo.findById(id).get();
    }
}
