package com.portoflio.api.dao;

import com.portoflio.api.models.Illustration;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
    List<Illustration> findByVisibilityTrue();
    List<Illustration> findAll(Specification<Illustration> specification);

}