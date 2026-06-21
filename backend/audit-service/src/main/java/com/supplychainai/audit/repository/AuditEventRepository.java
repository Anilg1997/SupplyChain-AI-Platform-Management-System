package com.supplychainai.audit.repository;

import com.supplychainai.audit.model.AuditEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditEventRepository extends MongoRepository<AuditEvent, UUID> {
}
