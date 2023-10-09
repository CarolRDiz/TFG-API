package com.portoflio.api.dao;

import com.portoflio.api.models.Illustration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IllustrationRepository extends JpaRepository<Illustration, Long> {
}