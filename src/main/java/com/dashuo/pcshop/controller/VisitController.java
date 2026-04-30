package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.VisitCreateRequest;
import com.dashuo.pcshop.dto.VisitResponse;
import com.dashuo.pcshop.dto.VisitUpdateRequest;
import com.dashuo.pcshop.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visits")
@Tag(name = "Visit Management", description = "来访记录管理接口，用于记录客户进店、咨询、需求场景等信息")
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @Operation(
            summary = "创建来访记录",
            description = "创建一条客户来访记录。客户ID可以为空，用于匿名来访。"
    )
    @PostMapping
    public VisitResponse create(@RequestBody VisitCreateRequest request) {
        return visitService.create(request);
    }

    @Operation(
            summary = "查询全部来访记录",
            description = "按来访时间倒序返回全部来访记录。"
    )
    @GetMapping
    public List<VisitResponse> listAll() {
        return visitService.listAll();
    }

    @Operation(
            summary = "根据ID查询来访记录",
            description = "根据来访记录ID查询单条来访信息。"
    )
    @GetMapping("/{id}")
    public VisitResponse getById(
            @Parameter(description = "来访记录ID", example = "1")
            @PathVariable Long id
    ) {
        return visitService.getById(id);
    }

    @Operation(
            summary = "更新来访记录",
            description = "根据来访记录ID更新来访信息。"
    )
    @PutMapping("/{id}")
    public VisitResponse update(
            @Parameter(description = "来访记录ID", example = "1")
            @PathVariable Long id,
            @RequestBody VisitUpdateRequest request
    ) {
        return visitService.update(id, request);
    }

    @Operation(
            summary = "删除来访记录",
            description = "根据来访记录ID删除来访记录。后续如果 Visit 已关联 Quote 或 Order，应改为禁止删除或软删除。"
    )
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "来访记录ID", example = "1")
            @PathVariable Long id
    ) {
        visitService.delete(id);
        return "Visit deleted successfully, id: " + id;
    }

    @Operation(
            summary = "查询某个客户的来访记录",
            description = "根据客户ID查询该客户的全部来访记录。"
    )
    @GetMapping("/customer/{customerId}")
    public List<VisitResponse> listByCustomerId(
            @Parameter(description = "客户ID", example = "1")
            @PathVariable Long customerId
    ) {
        return visitService.listByCustomerId(customerId);
    }

    @Operation(
            summary = "查询某个员工接待的来访记录",
            description = "根据员工ID查询该员工接待过的来访记录。"
    )
    @GetMapping("/employee/{employeeId}")
    public List<VisitResponse> listByEmployeeId(
            @Parameter(description = "员工ID", example = "1")
            @PathVariable Long employeeId
    ) {
        return visitService.listByEmployeeId(employeeId);
    }

    @Operation(
            summary = "按场景查询来访记录",
            description = "根据 personal、company、other 等场景查询来访记录。"
    )
    @GetMapping("/scenario/{scenario}")
    public List<VisitResponse> listByScenario(
            @Parameter(description = "来访场景", example = "personal")
            @PathVariable String scenario
    ) {
        return visitService.listByScenario(scenario);
    }

    @Operation(
            summary = "按目的查询来访记录",
            description = "根据 gaming、office、repair_consulting 等目的查询来访记录。"
    )
    @GetMapping("/purpose/{purpose}")
    public List<VisitResponse> listByPurpose(
            @Parameter(description = "来访目的", example = "gaming")
            @PathVariable String purpose
    ) {
        return visitService.listByPurpose(purpose);
    }

    @Operation(
            summary = "按日期查询来访记录",
            description = "查询某一天的全部来访记录。日期格式：yyyy-MM-dd。"
    )
    @GetMapping("/date")
    public List<VisitResponse> listByDate(
            @Parameter(description = "日期，格式 yyyy-MM-dd", example = "2026-04-28")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return visitService.listByDate(date);
    }

    @Operation(
            summary = "按日期范围查询来访记录",
            description = "查询某个日期范围内的来访记录。startDate 和 endDate 格式均为 yyyy-MM-dd。"
    )
    @GetMapping("/date-range")
    public List<VisitResponse> listByDateRange(
            @Parameter(description = "开始日期，格式 yyyy-MM-dd", example = "2026-04-01")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @Parameter(description = "结束日期，格式 yyyy-MM-dd", example = "2026-04-30")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {
        return visitService.listByDateRange(startDate, endDate);
    }
}