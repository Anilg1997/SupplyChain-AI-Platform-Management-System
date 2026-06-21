package com.supplychainai.planning.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
 @Table(name = "plan_items", schema = "planning_schema")
public class PlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID planId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Double plannedQuantity;

    @Column(nullable = false)
    private Double actualQuantity;

    @Column(nullable = false)
    private LocalDateTime plannedDate;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPlanId() { return planId; }
    public void setPlanId(UUID planId) { this.planId = planId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public Double getPlannedQuantity() { return plannedQuantity; }
    public void setPlannedQuantity(Double plannedQuantity) { this.plannedQuantity = plannedQuantity; }

    public Double getActualQuantity() { return actualQuantity; }
    public void setActualQuantity(Double actualQuantity) { this.actualQuantity = actualQuantity; }

    public LocalDateTime getPlannedDate() { return plannedDate; }
    public void setPlannedDate(LocalDateTime plannedDate) { this.plannedDate = plannedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
