package com.portoflio.api.services.impl;

import com.portoflio.api.dao.ProductCategoryRepository;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import com.portoflio.api.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryRepository repository;

    @Override
    public void create (Product product, Category category){
        ProductCategory productCategory = new ProductCategory(product, category);
        repository.save(productCategory);
    };
    @Override
    public List<ProductCategory> findAll(){
        return repository.findAll();
    }

    @Override
    public void deleteSome (List<Long> ids) {
        for(Long id : ids)
        {
            Optional<ProductCategory> oProductCategory = repository.findById(id);
            if (oProductCategory.isPresent()) {
                repository.delete(oProductCategory.get());
            } else {
                throw new NotFoundException("ProductCategory not found");
            }
        }
    }
}
