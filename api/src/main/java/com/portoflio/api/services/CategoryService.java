package com.portoflio.api.services;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO create (CategoryCreateDTO newCategory);
    List<CategoryDTO> findAll();
}