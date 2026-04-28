package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.ContactMethodCreateRequest;
import com.dashuo.pcshop.dto.ContactMethodResponse;
import com.dashuo.pcshop.dto.ContactMethodUpdateRequest;
import com.dashuo.pcshop.service.ContactMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Contact Method Management", description = "客户联系方式管理接口")
public class ContactMethodController {

    private final ContactMethodService contactMethodService;

    public ContactMethodController(ContactMethodService contactMethodService) {
        this.contactMethodService = contactMethodService;
    }

    @Operation(
            summary = "给客户新增联系方式",
            description = "根据客户ID，为该客户新增电话、微信、邮箱或其他联系方式。"
    )
    @PostMapping("/api/customers/{customerId}/contacts")
    public ContactMethodResponse create(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long customerId,
            @RequestBody ContactMethodCreateRequest request
    ) {
        return contactMethodService.create(customerId, request);
    }

    @Operation(
            summary = "查询某个客户的全部联系方式",
            description = "根据客户ID，返回该客户的所有联系方式。"
    )
    @GetMapping("/api/customers/{customerId}/contacts")
    public List<ContactMethodResponse> listByCustomerId(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long customerId
    ) {
        return contactMethodService.listByCustomerId(customerId);
    }

    @Operation(
            summary = "根据ID查询联系方式",
            description = "根据联系方式ID查询单条联系方式记录。"
    )
    @GetMapping("/api/contact-methods/{id}")
    public ContactMethodResponse getById(
            @Parameter(description = "联系方式ID", example = "1")
            @PathVariable Long id
    ) {
        return contactMethodService.getById(id);
    }

    @Operation(
            summary = "更新联系方式",
            description = "根据联系方式ID更新联系方式类型、内容以及是否为主要联系方式。"
    )
    @PutMapping("/api/contact-methods/{id}")
    public ContactMethodResponse update(
            @Parameter(description = "联系方式ID", example = "1")
            @PathVariable Long id,
            @RequestBody ContactMethodUpdateRequest request
    ) {
        return contactMethodService.update(id, request);
    }

    @Operation(
            summary = "删除联系方式",
            description = "根据联系方式ID删除某条联系方式记录。"
    )
    @DeleteMapping("/api/contact-methods/{id}")
    public String delete(
            @Parameter(description = "联系方式ID", example = "1")
            @PathVariable Long id
    ) {
        contactMethodService.delete(id);
        return "Contact method deleted successfully, id: " + id;
    }
}