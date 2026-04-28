package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "配件标准信息响应数据")
public class PartResponse {

    @Schema(description = "配件ID", example = "1")
    private Long id;

    @Schema(description = "配件类别", example = "GPU")
    private String category;

    @Schema(description = "配件品牌", example = "ASUS")
    private String brand;

    @Schema(description = "配件型号", example = "RTX 4060 Dual")
    private String model;

    @Schema(description = "配件描述或备注", example = "适合中端游戏主机")
    private String description;

    @Schema(description = "是否启用", example = "true")
    private Boolean active;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间")
    private LocalDateTime updatedAt;
}