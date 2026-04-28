package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.PartCreateRequest;
import com.dashuo.pcshop.dto.PartResponse;
import com.dashuo.pcshop.dto.PartUpdateRequest;
import com.dashuo.pcshop.service.PartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@Tag(name = "Part Management", description = "配件标准库管理接口")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @Operation(
            summary = "新增配件标准信息",
            description = "新增一个标准配件记录。Part 只记录类别、品牌、型号等标准信息，不记录库存、价格或新旧状态。"
    )
    @PostMapping
    public PartResponse create(@RequestBody PartCreateRequest request) {
        return partService.create(request);
    }

    @Operation(
            summary = "查询全部配件",
            description = "返回系统中的全部配件标准信息，包括已停用的配件。"
    )
    @GetMapping
    public List<PartResponse> listAll() {
        return partService.listAll();
    }

    @Operation(
            summary = "查询启用中的配件",
            description = "只返回 active = true 的配件。通常前端下拉选择配件时使用这个接口。"
    )
    @GetMapping("/active")
    public List<PartResponse> listActive() {
        return partService.listActive();
    }

    @Operation(
            summary = "根据ID查询配件",
            description = "根据配件ID查询单个标准配件信息。"
    )
    @GetMapping("/{id}")
    public PartResponse getById(
            @Parameter(description = "配件ID", example = "1")
            @PathVariable Long id
    ) {
        return partService.getById(id);
    }

    @Operation(
            summary = "更新配件标准信息",
            description = "根据配件ID更新类别、品牌、型号、描述以及是否启用。"
    )
    @PutMapping("/{id}")
    public PartResponse update(
            @Parameter(description = "配件ID", example = "1")
            @PathVariable Long id,
            @RequestBody PartUpdateRequest request
    ) {
        return partService.update(id, request);
    }

    @Operation(
            summary = "停用配件",
            description = "将配件 active 设置为 false。为了保留历史订单和库存关联，不建议真实删除配件。"
    )
    @DeleteMapping("/{id}")
    public String deactivate(
            @Parameter(description = "配件ID", example = "1")
            @PathVariable Long id
    ) {
        partService.deactivate(id);
        return "Part deactivated successfully, id: " + id;
    }

    @Operation(
            summary = "搜索配件",
            description = "根据关键词搜索配件类别、品牌或型号。"
    )
    @GetMapping("/search")
    public List<PartResponse> search(
            @Parameter(description = "搜索关键词，例如 GPU、ASUS、4060", example = "4060")
            @RequestParam String keyword
    ) {
        return partService.search(keyword);
    }
}