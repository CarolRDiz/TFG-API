package com.portoflio.api.services;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    void create (Product product, Category category);
    List<ProductCategory> findAll();
}
