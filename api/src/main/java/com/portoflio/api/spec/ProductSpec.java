package com.portoflio.api.spec;

import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class ProductSpec {
    public static Specification<Product> getSpec(String name, String category_name) {
        return (((root, query, criteriaBuilder) -> {
            List<Predicate> predicate = new ArrayList<>();

            if (name!=null && !(name.isEmpty())){
                predicate.add(criteriaBuilder.equal(root.get("name"),name));
            }
            if (category_name!=null && !(category_name.isEmpty())){
                Join<ProductCategory, Product> productCategories = root.join("productCategories");
                //Join<Join<ProductCategory, Product>, Category> productCategoriesCategory = root.join("productCategories");
                predicate.add(criteriaBuilder.equal(productCategories.get("category").get("name"),category_name));
            }
            predicate.add(criteriaBuilder.equal(root.get("visibility"),true));

            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        }));
    }
}
