package com.supplychainai.quality.repository;

import com.supplychainai.quality.model.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InspectionReportRepository extends JpaRepository<InspectionReport, UUID> {
}
