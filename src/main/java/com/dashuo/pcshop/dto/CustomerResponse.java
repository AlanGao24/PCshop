package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "客户响应数据")
public class CustomerResponse {

    @Schema(description = "客户ID", example = "1")
    private Long id;

    @Schema(description = "客户姓名", example = "张三")
    private String name;

    @Schema(description = "客户来源渠道", example = "offline")
    private String channel;

    @Schema(description = "客户类型", example = "normal")
    private String customerType;

    @Schema(description = "客户备注", example = "客户主要咨询游戏主机配置")
    private String notes;

    @Schema(description = "客户历史总消费金额", example = "0.00")
    private BigDecimal totalSpent;

    @Schema(description = "客户创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "客户最后更新时间")
    private LocalDateTime updatedAt;
}