package com.portoflio.api.dao;

import com.portoflio.api.models.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll(Specification<Product> specification);
    Optional<Product> findByIdAndVisibilityTrue(Long id);
}
