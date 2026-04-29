package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建库存记录请求")
public class InventoryCreateRequest {

    @Schema(description = "配件ID，对应 Part 标准配件库中的ID", example = "1")
    private Long partId;

    @Schema(description = "库存状态，只能是 NEW 或 USED。NEW 表示全新，USED 表示二手", example = "NEW")
    private String conditionType;

    @Schema(description = "当前库存数量，不能小于0", example = "10")
    private Integer quantity;

    @Schema(description = "平均成本价，不能小于0", example = "2200.00")
    private BigDecimal avgCost;
}