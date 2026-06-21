package com.supplychainai.supplier.repository;

import com.supplychainai.supplier.model.SupplierContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierContactRepository extends JpaRepository<SupplierContact, UUID> {
}
