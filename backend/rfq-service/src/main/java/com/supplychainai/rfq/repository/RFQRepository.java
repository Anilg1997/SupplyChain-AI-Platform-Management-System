package com.supplychainai.rfq.repository;

import com.supplychainai.rfq.model.RFQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RFQRepository extends JpaRepository<RFQ, UUID> {
}
