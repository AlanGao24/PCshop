package com.dashuo.pcshop.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer")
@Schema(description = "客户信息实体，用于记录电脑店客户的基础资料")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "客户ID，由数据库自动生成", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "客户姓名。匿名客户可以为空或填写为“匿名客户”", example = "张三")
    private String name;

    @Schema(description = "客户来源渠道，例如线下、抖音、小红书、老客户介绍等", example = "offline")
    private String channel;

    @Column(name = "customer_type")
    @Schema(description = "客户类型，例如 normal、student、company、vip、anonymous", example = "normal")
    private String customerType;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "客户备注，例如购买偏好、沟通情况、特殊需求等", example = "客户主要咨询游戏主机配置")
    private String notes;

    @Column(name = "total_spent")
    @Schema(description = "客户历史总消费金额。该字段为冗余统计字段，由系统根据订单更新", example = "0.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "created_at")
    @Schema(description = "客户创建时间，由系统自动生成", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "客户最后更新时间，由系统自动生成", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();

        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }
    }
}