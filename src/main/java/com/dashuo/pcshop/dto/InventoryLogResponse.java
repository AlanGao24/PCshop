package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "库存流水响应数据")
public class InventoryLogResponse {

    @Schema(description = "库存流水ID", example = "1")
    private Long id;

    @Schema(description = "库存记录ID", example = "1")
    private Long inventoryId;

    @Schema(description = "配件ID", example = "1")
    private Long partId;

    @Schema(description = "配件类别", example = "GPU")
    private String partCategory;

    @Schema(description = "配件品牌", example = "ASUS")
    private String partBrand;

    @Schema(description = "配件型号", example = "RTX 4060 Dual")
    private String partModel;

    @Schema(description = "库存状态", example = "NEW")
    private String conditionType;

    @Schema(description = "库存变化数量。入库为正数，出库为负数", example = "10")
    private Integer changeQty;

    @Schema(description = "流水类型", example = "purchase")
    private String logType;

    @Schema(description = "单件成本", example = "2200.00")
    private BigDecimal unitCost;

    @Schema(description = "关联来源类型", example = "manual")
    private String referenceType;

    @Schema(description = "关联来源ID", example = "1")
    private Long referenceId;

    @Schema(description = "操作员工ID", example = "1")
    private Long createdBy;

    @Schema(description = "备注", example = "第一次采购入库")
    private String notes;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}