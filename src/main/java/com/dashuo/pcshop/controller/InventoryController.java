package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.InventoryCreateRequest;
import com.dashuo.pcshop.dto.InventoryResponse;
import com.dashuo.pcshop.dto.InventoryUpdateRequest;
import com.dashuo.pcshop.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory Management", description = "库存管理接口，用于管理配件的全新/二手当前库存")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(
            summary = "创建库存记录",
            description = "为某个标准配件创建一条库存记录。每个配件的 NEW 和 USED 库存各只能有一条记录。"
    )
    @PostMapping
    public InventoryResponse create(@RequestBody InventoryCreateRequest request) {
        return inventoryService.create(request);
    }

    @Operation(
            summary = "查询全部库存",
            description = "返回系统中所有配件的当前库存。"
    )
    @GetMapping
    public List<InventoryResponse> listAll() {
        return inventoryService.listAll();
    }

    @Operation(
            summary = "根据ID查询库存",
            description = "根据库存ID查询单条库存记录。"
    )
    @GetMapping("/{id}")
    public InventoryResponse getById(
            @Parameter(description = "库存ID", example = "1")
            @PathVariable Long id
    ) {
        return inventoryService.getById(id);
    }

    @Operation(
            summary = "查询某个配件的库存",
            description = "根据 Part ID 查询该配件的所有库存记录，例如 NEW 和 USED。"
    )
    @GetMapping("/part/{partId}")
    public List<InventoryResponse> listByPartId(
            @Parameter(description = "配件ID", example = "1")
            @PathVariable Long partId
    ) {
        return inventoryService.listByPartId(partId);
    }

    @Operation(
            summary = "按库存状态查询库存",
            description = "根据 NEW 或 USED 查询库存记录。"
    )
    @GetMapping("/condition/{conditionType}")
    public List<InventoryResponse> listByConditionType(
            @Parameter(description = "库存状态，只能是 NEW 或 USED", example = "NEW")
            @PathVariable String conditionType
    ) {
        return inventoryService.listByConditionType(conditionType);
    }

    @Operation(
            summary = "查询低库存配件",
            description = "查询库存数量小于或等于指定阈值的库存记录。"
    )
    @GetMapping("/low-stock")
    public List<InventoryResponse> listLowStock(
            @Parameter(description = "库存阈值", example = "3")
            @RequestParam Integer threshold
    ) {
        return inventoryService.listLowStock(threshold);
    }

    @Operation(
            summary = "更新库存记录",
            description = "直接更新当前库存数量和平均成本。注意：这是基础版库存管理，后续正式入库/出库应通过 InventoryLog 完成。"
    )
    @PutMapping("/{id}")
    public InventoryResponse update(
            @Parameter(description = "库存ID", example = "1")
            @PathVariable Long id,
            @RequestBody InventoryUpdateRequest request
    ) {
        return inventoryService.update(id, request);
    }

    @Operation(
            summary = "删除库存记录",
            description = "删除某条库存记录。真实业务中如果已有库存流水，后续不建议物理删除。"
    )
    @DeleteMapping("/{id}")
    public String delete(
            @Parameter(description = "库存ID", example = "1")
            @PathVariable Long id
    ) {
        inventoryService.delete(id);
        return "Inventory deleted successfully, id: " + id;
    }
}