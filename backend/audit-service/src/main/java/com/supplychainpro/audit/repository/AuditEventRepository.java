package com.supplychainpro.audit.repository;

import com.supplychainpro.audit.model.AuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditEventRepository extends MongoRepository<AuditEvent, UUID> {
}
