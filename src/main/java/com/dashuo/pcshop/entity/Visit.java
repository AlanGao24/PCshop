package com.dashuo.pcshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "visit")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 匿名来访允许 customer 为空。
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    /**
     * 当前阶段 Employee 模块还没正式开发，所以先存 employeeId。
     */
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime;

    @Column(name = "visit_type", length = 50)
    private String visitType;

    @Column(name = "people_count")
    private Integer peopleCount = 1;

    @Column(length = 50)
    private String scenario;

    @Column(length = 100)
    private String purpose;

    @Column(name = "has_clear_demand")
    private Boolean hasClearDemand = false;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (visitTime == null) {
            visitTime = now;
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (peopleCount == null || peopleCount <= 0) {
            peopleCount = 1;
        }

        if (hasClearDemand == null) {
            hasClearDemand = false;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();

        if (peopleCount == null || peopleCount <= 0) {
            peopleCount = 1;
        }

        if (hasClearDemand == null) {
            hasClearDemand = false;
        }
    }
}