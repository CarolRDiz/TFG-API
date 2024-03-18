package com.portoflio.api.services.impl;

import com.portoflio.api.dao.CategoryRepository;
import com.portoflio.api.dao.ProductRepository;
import com.portoflio.api.dto.CategoryCreateDTO;
import com.portoflio.api.dto.CategoryDTO;
import com.portoflio.api.dto.IllustrationDTO;
import com.portoflio.api.exceptions.NotFoundException;
import com.portoflio.api.models.Category;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import com.portoflio.api.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    private ModelMapper mapper = new ModelMapper();

    /*
    @Override
    public Category addProduct (Long id, Product product){
        Optional<Category> oCategory = categoryRepository.findById(id);
        if(oCategory.isPresent()){
            Category category = oCategory.get();
            System.out.println("CATEGORY_PRODUCTS:");
            Set<Product> categoryProducts = category.getProducts();
            System.out.println(categoryProducts);
            System.out.println(categoryProducts.add(product));
            categoryProducts.add(product);
            System.out.println("CATEGORY_PRODUCTS PLUS PRODUCT:");
            System.out.println(categoryProducts);
            category.setProducts(categoryProducts);
            System.out.println("CATEGORY:");
            category = categoryRepository.save(category);
            System.out.println(category);
            return category;
        }
        else {
            throw new NotFoundException("Category not found");
        }
    };
*/
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

    @Override
    public void  delete(Long id){
        Optional<Category> oCategory = categoryRepository.findById(id);
        if (oCategory.isPresent()) {
            categoryRepository.delete(oCategory.get());
        } else {
            throw new NotFoundException("Category not found");
        }
    }
    @Override
    public CategoryDTO updateCategoryByFields(Long id, Map<String, Object> fields){
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()){
            fields.forEach((key,value) -> {
                Field field = ReflectionUtils.findField(Category.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, category.get(), value);
            });
            categoryRepository.save(category.get());
            return this.mapper.map(category.get(), CategoryDTO.class);
        }
        else{
            throw new NotFoundException("Category not found");
        }
    }

}