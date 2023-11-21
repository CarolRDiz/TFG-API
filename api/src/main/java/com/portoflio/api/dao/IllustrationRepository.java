package com.portoflio.api.dao;

import com.portoflio.api.models.Category;
import com.portoflio.api.models.Illustration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
    List<Illustration> findByVisibilityTrue();
}