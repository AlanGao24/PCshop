package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "库存响应数据")
public class InventoryResponse {

    @Schema(description = "库存ID", example = "1")
    private Long id;

    @Schema(description = "配件ID", example = "1")
    private Long partId;

    @Schema(description = "配件类别", example = "GPU")
    private String partCategory;

    @Schema(description = "配件品牌", example = "ASUS")
    private String partBrand;

    @Schema(description = "配件型号", example = "RTX 4060 Dual")
    private String partModel;

    @Schema(description = "库存状态，NEW 表示全新，USED 表示二手", example = "NEW")
    private String conditionType;

    @Schema(description = "当前库存数量", example = "10")
    private Integer quantity;

    @Schema(description = "平均成本价", example = "2200.00")
    private BigDecimal avgCost;

    @Schema(description = "最后更新时间")
    private LocalDateTime lastUpdatedAt;
}