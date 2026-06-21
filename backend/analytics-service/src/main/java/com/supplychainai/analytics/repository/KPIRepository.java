package com.supplychainai.analytics.repository;

import com.supplychainai.analytics.model.KPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KPIRepository extends JpaRepository<KPI, UUID> {
}
