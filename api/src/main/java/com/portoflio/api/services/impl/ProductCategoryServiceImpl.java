package com.portoflio.api.services.impl;

import com.portoflio.api.dao.ProductCategoryRepository;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import com.portoflio.api.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryRepository productCategoryRepository;

    @Override
    public void create (Product product, Category category){
        ProductCategory productCategory = new ProductCategory(product, category);
        productCategoryRepository.save(productCategory);
    };
    @Override
    public List<ProductCategory> findAll(){
        return productCategoryRepository.findAll();
    }

}
