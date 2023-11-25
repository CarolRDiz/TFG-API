package com.portoflio.api.services;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CategoryService {
    CategoryDTO create (CategoryCreateDTO newCategory);
    List<CategoryDTO> findAll();
    void delete(Long id);
    CategoryDTO updateCategoryByFields(Long id, Map<String, Object> fields);

}