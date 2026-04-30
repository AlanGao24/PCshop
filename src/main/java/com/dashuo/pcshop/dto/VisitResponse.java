package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "来访记录响应数据")
public class VisitResponse {

    @Schema(description = "来访记录ID", example = "1")
    private Long id;

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "客户姓名", example = "张三")
    private String customerName;

    @Schema(description = "接待员工ID", example = "1")
    private Long employeeId;

    @Schema(description = "来访时间")
    private LocalDateTime visitTime;

    @Schema(description = "来访类型", example = "single")
    private String visitType;

    @Schema(description = "同行人数", example = "1")
    private Integer peopleCount;

    @Schema(description = "来访场景", example = "personal")
    private String scenario;

    @Schema(description = "来访目的", example = "gaming")
    private String purpose;

    @Schema(description = "是否有明确需求", example = "true")
    private Boolean hasClearDemand;

    @Schema(description = "备注", example = "客户想咨询一台 6000 元左右的游戏主机")
    private String notes;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间")
    private LocalDateTime updatedAt;
}