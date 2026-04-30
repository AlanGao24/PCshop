package com.dashuo.pcshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_log")
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 当前库存记录。
     * 某些历史数据或异常情况下 inventory_id 可能为空，所以数据库中允许 SET NULL。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    /**
     * 标准配件。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(name = "condition_type", nullable = false, length = 10)
    private String conditionType;

    @Column(name = "change_qty", nullable = false)
    private Integer changeQty;

    @Column(name = "log_type", nullable = false, length = 20)
    private String logType;

    @Column(name = "unit_cost")
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (unitCost == null) {
            unitCost = BigDecimal.ZERO;
        }

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}