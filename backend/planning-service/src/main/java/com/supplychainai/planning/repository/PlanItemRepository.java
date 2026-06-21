package com.supplychainai.planning.repository;

import com.supplychainai.planning.model.PlanItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanItemRepository extends JpaRepository<PlanItem, UUID> {
}
