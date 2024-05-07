package com.portoflio.api.services;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    CategoryDTO create (CategoryCreateDTO newCategory);
    List<CategoryDTO> findAll();
    void delete(Long id);
    CategoryDTO updateCategoryByFields(Long id, Map<String, Object> fields);

}