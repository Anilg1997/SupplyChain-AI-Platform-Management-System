package com.supplychainai.returns.repository;

import com.supplychainai.returns.model.ReturnOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReturnOrderRepository extends JpaRepository<ReturnOrder, UUID> {
}
