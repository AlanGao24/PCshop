package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "客户联系方式响应数据")
public class ContactMethodResponse {

    @Schema(description = "联系方式ID", example = "1")
    private Long id;

    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    @Schema(description = "联系方式类型", example = "wechat")
    private String contactType;

    @Schema(description = "联系方式具体内容", example = "alan_wechat")
    private String contactValue;

    @Schema(description = "是否为主要联系方式", example = "true")
    private Boolean primaryContact;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "最后更新时间")
    private LocalDateTime updatedAt;
}