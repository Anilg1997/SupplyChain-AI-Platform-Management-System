package com.supplychainai.cost.repository;

import com.supplychainai.cost.model.CostEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CostEntryRepository extends JpaRepository<CostEntry, UUID> {
}
