package com.portoflio.api.dao;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.Illustration;
import com.portoflio.api.models.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
    List<Illustration> findByVisibilityTrue();
    List<Illustration> findAll(Specification<Illustration> specification);

}