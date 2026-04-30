package com.dashuo.pcshop.controller;

import com.dashuo.pcshop.dto.InventoryChangeRequest;
import com.dashuo.pcshop.dto.InventoryLogCreateRequest;
import com.dashuo.pcshop.dto.InventoryLogResponse;
import com.dashuo.pcshop.service.InventoryLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory-logs")
@Tag(name = "Inventory Log Management", description = "库存流水管理接口，用于记录采购、销售、回收、调整和退货等库存变化")
public class InventoryLogController {

    private final InventoryLogService inventoryLogService;

    public InventoryLogController(InventoryLogService inventoryLogService) {
        this.inventoryLogService = inventoryLogService;
    }

    @Operation(
            summary = "创建库存流水",
            description = "通用库存流水接口。会自动更新当前库存，并写入库存流水。changeQty 入库为正数，出库为负数。"
    )
    @PostMapping
    public InventoryLogResponse createLog(@RequestBody InventoryLogCreateRequest request) {
        return inventoryLogService.createLog(request);
    }

    @Operation(
            summary = "采购入库",
            description = "用于全新件采购入库。quantity 填正数，系统会自动增加库存并重新计算平均成本。"
    )
    @PostMapping("/purchase")
    public InventoryLogResponse purchase(@RequestBody InventoryChangeRequest request) {
        return inventoryLogService.purchase(request);
    }

    @Operation(
            summary = "二手回收入库",
            description = "用于二手配件回收入库。通常 conditionType 为 USED。quantity 填正数，系统会自动增加库存。"
    )
    @PostMapping("/recycle")
    public InventoryLogResponse recycle(@RequestBody InventoryChangeRequest request) {
        return inventoryLogService.recycle(request);
    }

    @Operation(
            summary = "销售出库",
            description = "用于销售出库。quantity 填正数，系统会自动转换为负数并扣减库存。库存不足会返回错误。"
    )
    @PostMapping("/sale")
    public InventoryLogResponse sale(@RequestBody InventoryChangeRequest request) {
        return inventoryLogService.sale(request);
    }

    @Operation(
            summary = "退货入库",
            description = "用于客户退货或销售退回。quantity 填正数，系统会自动增加库存。"
    )
    @PostMapping("/return")
    public InventoryLogResponse returnStock(@RequestBody InventoryChangeRequest request) {
        return inventoryLogService.returnStock(request);
    }

    @Operation(
            summary = "人工调整库存",
            description = "用于盘点调整。changeQty 可以为正数或负数，系统会同步修改当前库存并记录流水。"
    )
    @PostMapping("/adjust")
    public InventoryLogResponse adjust(@RequestBody InventoryLogCreateRequest request) {
        return inventoryLogService.adjust(request);
    }

    @Operation(
            summary = "查询全部库存流水",
            description = "按创建时间倒序返回全部库存流水。"
    )
    @GetMapping
    public List<InventoryLogResponse> listAll() {
        return inventoryLogService.listAll();
    }

    @Operation(
            summary = "根据ID查询库存流水",
            description = "根据库存流水ID查询单条记录。"
    )
    @GetMapping("/{id}")
    public InventoryLogResponse getById(
            @Parameter(description = "库存流水ID", example = "1")
            @PathVariable Long id
    ) {
        return inventoryLogService.getById(id);
    }

    @Operation(
            summary = "查询某个配件的库存流水",
            description = "根据 Part ID 查询该配件相关的所有库存流水。"
    )
    @GetMapping("/part/{partId}")
    public List<InventoryLogResponse> listByPartId(
            @Parameter(description = "配件ID", example = "1")
            @PathVariable Long partId
    ) {
        return inventoryLogService.listByPartId(partId);
    }

    @Operation(
            summary = "查询某条库存记录的流水",
            description = "根据 Inventory ID 查询该库存记录的全部流水。"
    )
    @GetMapping("/inventory/{inventoryId}")
    public List<InventoryLogResponse> listByInventoryId(
            @Parameter(description = "库存ID", example = "1")
            @PathVariable Long inventoryId
    ) {
        return inventoryLogService.listByInventoryId(inventoryId);
    }

    @Operation(
            summary = "按流水类型查询库存流水",
            description = "根据 purchase、sale、recycle、adjust、return 查询库存流水。"
    )
    @GetMapping("/type/{logType}")
    public List<InventoryLogResponse> listByLogType(
            @Parameter(description = "流水类型", example = "purchase")
            @PathVariable String logType
    ) {
        return inventoryLogService.listByLogType(logType);
    }
}