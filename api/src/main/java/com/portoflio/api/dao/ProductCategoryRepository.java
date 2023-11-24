package com.portoflio.api.dao;

import com.portoflio.api.models.Product;
import com.portoflio.api.models.ProductCategory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}
