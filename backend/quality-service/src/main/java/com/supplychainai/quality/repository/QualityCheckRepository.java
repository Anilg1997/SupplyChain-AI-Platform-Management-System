package com.supplychainai.quality.repository;

import com.supplychainai.quality.model.QualityCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QualityCheckRepository extends JpaRepository<QualityCheck, UUID> {
}
