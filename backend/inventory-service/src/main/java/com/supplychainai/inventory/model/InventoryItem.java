package com.supplychainai.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
 @Table(name = "inventory_items", schema = "inventory_schema")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID warehouseId;

    @Column(nullable = false)
    private Double quantityOnHand;

    @Column(nullable = false)
    private Double quantityReserved;

    @Column(nullable = false)
    private Double quantityAvailable;

    @Column(nullable = false)
    private Double reorderPoint;

    @Column(nullable = false)
    private String unitOfMeasure;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    void onUpdate() { updatedAt = LocalDateTime.now(); }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public UUID getWarehouseId() { return warehouseId; }
    public void setWarehouseId(UUID warehouseId) { this.warehouseId = warehouseId; }

    public Double getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(Double quantityOnHand) { this.quantityOnHand = quantityOnHand; }

    public Double getQuantityReserved() { return quantityReserved; }
    public void setQuantityReserved(Double quantityReserved) { this.quantityReserved = quantityReserved; }

    public Double getQuantityAvailable() { return quantityAvailable; }
    public void setQuantityAvailable(Double quantityAvailable) { this.quantityAvailable = quantityAvailable; }

    public Double getReorderPoint() { return reorderPoint; }
    public void setReorderPoint(Double reorderPoint) { this.reorderPoint = reorderPoint; }

    public String getUnitOfMeasure() { return unitOfMeasure; }
    public void setUnitOfMeasure(String unitOfMeasure) { this.unitOfMeasure = unitOfMeasure; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
