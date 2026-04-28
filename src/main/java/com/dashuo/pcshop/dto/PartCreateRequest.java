package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建配件标准信息请求")
public class PartCreateRequest {

    @Schema(description = "配件类别，例如 CPU、GPU、RAM、SSD、motherboard、case、power_supply", example = "GPU")
    private String category;

    @Schema(description = "配件品牌，例如 ASUS、MSI、Intel、AMD、Kingston", example = "ASUS")
    private String brand;

    @Schema(description = "配件型号，例如 RTX 4060 Dual、i5-14600KF", example = "RTX 4060 Dual")
    private String model;

    @Schema(description = "配件描述或备注", example = "适合中端游戏主机")
    private String description;
}