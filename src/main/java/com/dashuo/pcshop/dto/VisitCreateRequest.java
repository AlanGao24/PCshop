package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "创建来访记录请求")
public class VisitCreateRequest {

    @Schema(description = "客户ID。匿名来访可以为空", example = "1")
    private Long customerId;

    @Schema(description = "接待员工ID。没有员工系统时可暂时为空", example = "1")
    private Long employeeId;

    @Schema(description = "来访时间。如果为空，系统默认使用当前时间", example = "2026-04-28T15:30:00")
    private LocalDateTime visitTime;

    @Schema(description = "来访类型，例如 single、couple、with_child、with_parent、friends、other", example = "single")
    private String visitType;

    @Schema(description = "同行人数", example = "1")
    private Integer peopleCount;

    @Schema(description = "来访场景，例如 personal、company、other", example = "personal")
    private String scenario;

    @Schema(description = "来访目的，例如 gaming、home、office、repair_consulting、company_business、other", example = "gaming")
    private String purpose;

    @Schema(description = "是否有明确需求", example = "true")
    private Boolean hasClearDemand;

    @Schema(description = "备注，例如客户预算、需求、沟通情况等", example = "客户想咨询一台 6000 元左右的游戏主机")
    private String notes;
}