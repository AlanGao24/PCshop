package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "库存变动请求")
public class InventoryChangeRequest {

    @Schema(description = "配件ID，对应 Part 标准配件库中的ID", example = "1")
    private Long partId;

    @Schema(description = "库存状态，只能是 NEW 或 USED", example = "NEW")
    private String conditionType;

    @Schema(description = "库存变化数量。采购、回收、退货通常为正数；销售接口中这里也填正数，系统会自动转为负数", example = "5")
    private Integer quantity;

    @Schema(description = "本次库存变动的单件成本", example = "2200.00")
    private BigDecimal unitCost;

    @Schema(description = "关联来源类型，例如 manual、order、order_item、stocktake、other", example = "manual")
    private String referenceType;

    @Schema(description = "关联来源ID，例如订单ID或订单项ID。没有可为空", example = "1")
    private Long referenceId;

    @Schema(description = "操作员工ID。没有登录系统时可暂时为空", example = "1")
    private Long createdBy;

    @Schema(description = "备注", example = "采购入库测试")
    private String notes;
}