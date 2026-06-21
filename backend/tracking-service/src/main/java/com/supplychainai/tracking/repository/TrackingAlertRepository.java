package com.supplychainai.tracking.repository;

import com.supplychainai.tracking.model.TrackingAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackingAlertRepository extends JpaRepository<TrackingAlert, UUID> {
}
