package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新配件标准信息请求")
public class PartUpdateRequest {

    @Schema(description = "配件类别，例如 CPU、GPU、RAM、SSD、motherboard、case、power_supply", example = "GPU")
    private String category;

    @Schema(description = "配件品牌", example = "ASUS")
    private String brand;

    @Schema(description = "配件型号", example = "RTX 4060 Dual OC")
    private String model;

    @Schema(description = "配件描述或备注", example = "更新后的型号备注")
    private String description;

    @Schema(description = "是否启用。false 表示停用该标准配件", example = "true")
    private Boolean active;
}