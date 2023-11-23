package com.portoflio.api.services;

import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    CategoryDTO create (CategoryCreateDTO newCategory);
    List<CategoryDTO> findAll();
    Set<Product> delete(Long id);
    Category addProduct (Long id, Product product);
}