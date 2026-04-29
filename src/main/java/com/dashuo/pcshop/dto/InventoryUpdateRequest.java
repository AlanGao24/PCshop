package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "更新库存记录请求")
public class InventoryUpdateRequest {

    @Schema(description = "当前库存数量，不能小于0", example = "8")
    private Integer quantity;

    @Schema(description = "平均成本价，不能小于0", example = "2150.00")
    private BigDecimal avgCost;
}