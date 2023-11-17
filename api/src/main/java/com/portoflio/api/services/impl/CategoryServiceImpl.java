package com.portoflio.api.services.impl;

import com.portoflio.api.dao.CategoryRepository;
import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;
import com.portoflio.api.models.Category;
import com.portoflio.api.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    private ModelMapper mapper = new ModelMapper();

    public CategoryDTO create (CategoryCreateDTO newCategory){
        Optional<Category> oCategory = categoryRepository.findByName(newCategory.getName());
        if(oCategory.isPresent()){
            throw new IllegalStateException("Category's name is used");
        }
        Category category = this.mapper.map(newCategory, Category.class);
        Category categorySaved = categoryRepository.save(category);
        return this.mapper.map(categorySaved, CategoryDTO.class);
    }
    public List<CategoryDTO> findAll(){
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> dtos = categories
                .stream()
                .map(category -> mapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        return dtos;
    }
}