package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.CustomerCreateRequest;
import com.dashuo.pcshop.dto.CustomerResponse;
import com.dashuo.pcshop.dto.CustomerUpdateRequest;
import com.dashuo.pcshop.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer Management", description = "客户管理接口，包括客户新增、查询、修改和删除")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "创建新客户",
            description = "用于新增客户基础信息。匿名客户也可以创建，联系方式可以后续通过 ContactMethod 模块补充。"
    )
    @PostMapping
    public CustomerResponse create(@RequestBody CustomerCreateRequest request) {
        return customerService.create(request);
    }

    @Operation(
            summary = "查询全部客户",
            description = "返回系统中所有客户的基础信息。"
    )
    @GetMapping
    public List<CustomerResponse> listAll() {
        return customerService.listAll();
    }

    @Operation(
            summary = "根据ID查询客户",
            description = "根据客户ID查询某一个客户的详细信息。"
    )
    @GetMapping("/{id}")
    public CustomerResponse getById(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long id
    ) {
        return customerService.getById(id);
    }

    @Operation(
            summary = "更新客户信息",
            description = "根据客户ID更新客户姓名、渠道、客户类型和备注。totalSpent 不通过该接口手动修改。"
    )
    @PutMapping("/{id}")
    public CustomerResponse update(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long id,
            @RequestBody CustomerUpdateRequest request
    ) {
        return customerService.update(id, request);
    }

    @Operation(
            summary = "删除客户",
            description = "根据客户ID删除客户。注意：真实业务中如果客户已经有关联订单，后续应改为禁用或软删除。"
    )
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long id
    ) {
        customerService.delete(id);
        return "Customer deleted successfully, id: " + id;
    }

    @Operation(
            summary = "根据姓名搜索客户",
            description = "根据客户姓名进行模糊搜索。"
    )
    @GetMapping("/search")
    public List<CustomerResponse> searchByName(
            @Parameter(description = "客户姓名关键词", example = "张")
            @RequestParam String name
    ) {
        return customerService.searchByName(name);
    }

    @Operation(
            summary = "根据联系方式搜索客户",
            description = "通过手机号、微信号、邮箱或其他联系方式关键词搜索客户，用于客户再次来访时快速查找历史业务记录。"
    )
    @GetMapping("/search-by-contact")
    public List<CustomerResponse> searchByContact(
            @Parameter(description = "联系方式关键词，例如手机号、微信号、邮箱片段", example = "alan_wechat")
            @RequestParam String keyword
    ) {
        return customerService.searchByContact(keyword);
    }

}