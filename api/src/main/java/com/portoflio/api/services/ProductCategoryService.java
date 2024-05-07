package com.portoflio.api.services;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import java.util.List;

public interface ProductCategoryService {
    void create (Product product, Category category);
    void createList (Long product_id, List<Long> category_ids);
    List<ProductCategory> findAll();
    void deleteSome (Long product_id, List<Long> category_ids);
    void delete (Long id);

}
