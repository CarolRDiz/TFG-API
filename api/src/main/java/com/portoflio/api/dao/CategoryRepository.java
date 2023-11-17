package com.portoflio.api.dao;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.Illustration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>  {
    Optional<Category> findByName(String name);
}
