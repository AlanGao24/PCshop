package com.dashuo.pcshop.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "创建客户请求")
public class CustomerCreateRequest {

    @Schema(description = "客户姓名。匿名客户可以填写为“匿名客户”", example = "张三")
    private String name;

    @Schema(description = "客户来源渠道，例如 offline、douyin、xiaohongshu、referral、other", example = "offline")
    private String channel;

    @Schema(description = "客户类型，例如 normal、student、company、vip、anonymous", example = "normal")
    private String customerType;

    @Schema(description = "客户备注，例如购买偏好、沟通情况、特殊需求等", example = "客户主要咨询游戏主机配置")
    private String notes;
}