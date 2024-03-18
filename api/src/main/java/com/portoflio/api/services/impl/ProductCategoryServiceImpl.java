package com.portoflio.api.services.impl;

import com.portoflio.api.dao.CategoryRepository;
import com.portoflio.api.dao.ProductCategoryRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import com.portoflio.api.services.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    ProductCategoryRepository repository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public void create (Product product, Category category){
        ProductCategory productCategory = new ProductCategory(product, category);
        repository.save(productCategory);
    };

    @Override
    public void createList (Long product_id, List<Long> category_ids){
        Optional<Product> oProduct = productRepository.findById(product_id);
        if( oProduct.isPresent()){
            for(Long c_id : category_ids) {
                Optional<Category> oCategory = categoryRepository.findById(c_id);
                if (oCategory.isPresent()){
                    ProductCategory productCategory = new ProductCategory(oProduct.get(), oCategory.get());
                    repository.save(productCategory);
                } else {
                    throw new NotFoundException("Category not found");
                }
            }
        } else {
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    public List<ProductCategory> findAll(){
        return repository.findAll();
    }

    @Override
    public void deleteSome (Long product_id, List<Long> category_ids) {
        System.out.println("DELETE SOME");
        for(Long c_id : category_ids) {
            System.out.println(c_id);
            Optional<ProductCategory> pc = repository.findByProductIdAndCategoryId(product_id,c_id);
            if (pc.isPresent()) {
                System.out.println("RELACION ENCONTRADA");
                System.out.println(pc.get().getId());
                repository.delete(pc.get());
                Optional<ProductCategory> oTest = repository.findById(pc.get().getId());
                if (oTest.isPresent()){System.out.println("NO SE HA BORRADO");}
            } else {
                throw new NotFoundException("ProductCategory not found");
            }
        }
    }
    @Override
    public void delete (Long id){
        Optional<ProductCategory> oProductCategory = repository.findById(id);
        if (oProductCategory.isPresent()) {
            System.out.println("RELACION ENCONTRADA");
            System.out.println(oProductCategory.get().getId());
            repository.delete(oProductCategory.get());
            Optional<ProductCategory> oTest = repository.findById(id);
            if (oTest.isPresent()){System.out.println("NO SE HA BORRADO");}
        } else {
            throw new NotFoundException("ProductCategory not found");
        }


    }
}
