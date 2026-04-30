package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建库存流水请求")
public class InventoryLogCreateRequest {

    @Schema(description = "配件ID，对应 Part 标准配件库中的ID", example = "1")
    private Long partId;

    @Schema(description = "库存状态，只能是 NEW 或 USED", example = "NEW")
    private String conditionType;

    @Schema(description = "库存变化数量。入库为正数，出库为负数，不能为0", example = "10")
    private Integer changeQty;

    @Schema(description = "流水类型：purchase采购入库、sale销售出库、recycle二手回收、adjust人工调整、return退货入库", example = "purchase")
    private String logType;

    @Schema(description = "本次库存变动的单件成本。销售出库时可传当前成本，也可为0", example = "2200.00")
    private BigDecimal unitCost;

    @Schema(description = "关联来源类型，例如 manual、order、order_item、stocktake、other", example = "manual")
    private String referenceType;

    @Schema(description = "关联来源ID，例如订单ID或订单项ID。没有可为空", example = "1")
    private Long referenceId;

    @Schema(description = "操作员工ID。没有登录系统时可暂时为空", example = "1")
    private Long createdBy;

    @Schema(description = "备注", example = "第一次采购入库")
    private String notes;
}