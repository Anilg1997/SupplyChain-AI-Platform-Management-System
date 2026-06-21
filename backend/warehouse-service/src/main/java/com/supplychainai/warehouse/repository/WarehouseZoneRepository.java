package com.supplychainai.warehouse.repository;

import com.supplychainai.warehouse.model.WarehouseZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseZoneRepository extends JpaRepository<WarehouseZone, UUID> {
}
