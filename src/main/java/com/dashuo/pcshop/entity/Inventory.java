package com.dashuo.pcshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_part_condition",
                        columnNames = {"part_id", "condition_type"}
                )
        }
)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 一个库存记录对应一个标准配件。
     * 同一个 Part 可以有两条库存：NEW 和 USED。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "condition_type", nullable = false, length = 10)
    private String conditionType;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "avg_cost")
    private BigDecimal avgCost = BigDecimal.ZERO;

    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    public void prePersist() {
        if (quantity == null) {
            quantity = 0;
        }

        if (avgCost == null) {
            avgCost = BigDecimal.ZERO;
        }

        if (lastUpdatedAt == null) {
            lastUpdatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (quantity == null) {
            quantity = 0;
        }

        if (avgCost == null) {
            avgCost = BigDecimal.ZERO;
        }

        lastUpdatedAt = LocalDateTime.now();
    }
}