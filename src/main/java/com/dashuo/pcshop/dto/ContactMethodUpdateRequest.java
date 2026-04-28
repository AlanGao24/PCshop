package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新客户联系方式请求")
public class ContactMethodUpdateRequest {

    @Schema(description = "联系方式类型，例如 phone、wechat、email、telegram、other", example = "phone")
    private String contactType;

    @Schema(description = "联系方式具体内容，例如手机号、微信号、邮箱地址等", example = "91234567")
    private String contactValue;

    @Schema(description = "是否为主要联系方式", example = "false")
    private Boolean primaryContact;
}