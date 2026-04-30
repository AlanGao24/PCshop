package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.InventoryChangeRequest;
import com.dashuo.pcshop.dto.InventoryLogCreateRequest;
import com.dashuo.pcshop.dto.InventoryLogResponse;
import com.dashuo.pcshop.entity.Inventory;
import com.dashuo.pcshop.entity.InventoryLog;
import com.dashuo.pcshop.entity.Part;
import com.dashuo.pcshop.exception.BusinessException;
import com.dashuo.pcshop.repository.InventoryLogRepository;
import com.dashuo.pcshop.repository.InventoryRepository;
import com.dashuo.pcshop.repository.PartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InventoryLogService {

    private final InventoryLogRepository inventoryLogRepository;
    private final InventoryRepository inventoryRepository;
    private final PartRepository partRepository;

    public InventoryLogService(
            InventoryLogRepository inventoryLogRepository,
            InventoryRepository inventoryRepository,
            PartRepository partRepository
    ) {
        this.inventoryLogRepository = inventoryLogRepository;
        this.inventoryRepository = inventoryRepository;
        this.partRepository = partRepository;
    }

    @Transactional
    public InventoryLogResponse createLog(InventoryLogCreateRequest request) {
        validateCreateRequest(request);

        Long partId = request.getPartId();
        String conditionType = normalizeConditionType(request.getConditionType());
        String logType = normalizeLogType(request.getLogType());
        Integer changeQty = request.getChangeQty();
        BigDecimal unitCost = normalizeMoney(request.getUnitCost());

        Part part = findPartById(partId);
        Inventory inventory = findOrCreateInventory(part, conditionType);

        updateInventoryByChange(inventory, changeQty, unitCost, logType);

        Inventory savedInventory = inventoryRepository.save(inventory);

        InventoryLog log = new InventoryLog();
        log.setInventory(savedInventory);
        log.setPart(part);
        log.setConditionType(conditionType);
        log.setChangeQty(changeQty);
        log.setLogType(logType);
        log.setUnitCost(unitCost);
        log.setReferenceType(normalizeText(request.getReferenceType()));
        log.setReferenceId(request.getReferenceId());
        log.setCreatedBy(request.getCreatedBy());
        log.setNotes(normalizeText(request.getNotes()));

        InventoryLog savedLog = inventoryLogRepository.save(log);
        return toResponse(savedLog);
    }

    @Transactional
    public InventoryLogResponse purchase(InventoryChangeRequest request) {
        InventoryLogCreateRequest logRequest = buildCreateRequest(request, "purchase", true);
        return createLog(logRequest);
    }

    @Transactional
    public InventoryLogResponse recycle(InventoryChangeRequest request) {
        InventoryLogCreateRequest logRequest = buildCreateRequest(request, "recycle", true);
        return createLog(logRequest);
    }

    @Transactional
    public InventoryLogResponse returnStock(InventoryChangeRequest request) {
        InventoryLogCreateRequest logRequest = buildCreateRequest(request, "return", true);
        return createLog(logRequest);
    }

    @Transactional
    public InventoryLogResponse sale(InventoryChangeRequest request) {
        InventoryLogCreateRequest logRequest = buildCreateRequest(request, "sale", false);
        return createLog(logRequest);
    }

    @Transactional
    public InventoryLogResponse adjust(InventoryLogCreateRequest request) {
        if (request.getLogType() == null || request.getLogType().isBlank()) {
            request.setLogType("adjust");
        }

        if (!normalizeLogType(request.getLogType()).equals("adjust")) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Adjust endpoint only accepts logType adjust"
            );
        }

        return createLog(request);
    }

    public List<InventoryLogResponse> listAll() {
        return inventoryLogRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InventoryLogResponse getById(Long id) {
        InventoryLog log = findLogById(id);
        return toResponse(log);
    }

    public List<InventoryLogResponse> listByPartId(Long partId) {
        findPartById(partId);

        return inventoryLogRepository.findByPartIdOrderByCreatedAtDesc(partId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventoryLogResponse> listByInventoryId(Long inventoryId) {
        inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Inventory not found, id: " + inventoryId
                ));

        return inventoryLogRepository.findByInventoryIdOrderByCreatedAtDesc(inventoryId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventoryLogResponse> listByLogType(String logType) {
        String normalizedLogType = normalizeLogType(logType);

        return inventoryLogRepository.findByLogTypeOrderByCreatedAtDesc(normalizedLogType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private InventoryLogCreateRequest buildCreateRequest(
            InventoryChangeRequest request,
            String logType,
            boolean positiveChange
    ) {
        validateChangeRequest(request);

        InventoryLogCreateRequest logRequest = new InventoryLogCreateRequest();
        logRequest.setPartId(request.getPartId());
        logRequest.setConditionType(request.getConditionType());
        logRequest.setLogType(logType);

        int quantity = request.getQuantity();

        if (positiveChange) {
            logRequest.setChangeQty(quantity);
        } else {
            logRequest.setChangeQty(-quantity);
        }

        logRequest.setUnitCost(request.getUnitCost());
        logRequest.setReferenceType(request.getReferenceType());
        logRequest.setReferenceId(request.getReferenceId());
        logRequest.setCreatedBy(request.getCreatedBy());
        logRequest.setNotes(request.getNotes());

        return logRequest;
    }

    private void validateCreateRequest(InventoryLogCreateRequest request) {
        if (request.getPartId() == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Part ID is required"
            );
        }

        normalizeConditionType(request.getConditionType());
        normalizeLogType(request.getLogType());

        if (request.getChangeQty() == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Change quantity is required"
            );
        }

        if (request.getChangeQty() == 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Change quantity cannot be zero"
            );
        }

        normalizeMoney(request.getUnitCost());
    }

    private void validateChangeRequest(InventoryChangeRequest request) {
        if (request.getPartId() == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Part ID is required"
            );
        }

        normalizeConditionType(request.getConditionType());

        if (request.getQuantity() == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Quantity is required"
            );
        }

        if (request.getQuantity() <= 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Quantity must be greater than zero"
            );
        }

        normalizeMoney(request.getUnitCost());
    }

    private void updateInventoryByChange(
            Inventory inventory,
            Integer changeQty,
            BigDecimal unitCost,
            String logType
    ) {
        int oldQuantity = inventory.getQuantity() == null ? 0 : inventory.getQuantity();
        BigDecimal oldAvgCost = inventory.getAvgCost() == null ? BigDecimal.ZERO : inventory.getAvgCost();

        int newQuantity = oldQuantity + changeQty;

        if (newQuantity < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Insufficient stock. Current quantity: "
                            + oldQuantity
                            + ", change quantity: "
                            + changeQty
            );
        }

        BigDecimal newAvgCost = calculateNewAvgCost(
                oldQuantity,
                oldAvgCost,
                changeQty,
                unitCost,
                logType,
                newQuantity
        );

        inventory.setQuantity(newQuantity);
        inventory.setAvgCost(newAvgCost);
    }

    private BigDecimal calculateNewAvgCost(
            int oldQuantity,
            BigDecimal oldAvgCost,
            int changeQty,
            BigDecimal unitCost,
            String logType,
            int newQuantity
    ) {
        if (newQuantity == 0) {
            return BigDecimal.ZERO;
        }

        boolean isIncoming = changeQty > 0;

        if (!isIncoming) {
            return oldAvgCost;
        }

        if (logType.equals("adjust")) {
            return oldAvgCost;
        }

        BigDecimal oldTotalCost = oldAvgCost.multiply(BigDecimal.valueOf(oldQuantity));
        BigDecimal incomingTotalCost = unitCost.multiply(BigDecimal.valueOf(changeQty));
        BigDecimal newTotalCost = oldTotalCost.add(incomingTotalCost);

        return newTotalCost.divide(
                BigDecimal.valueOf(newQuantity),
                2,
                RoundingMode.HALF_UP
        );
    }

    private Inventory findOrCreateInventory(Part part, String conditionType) {
        return inventoryRepository.findByPartIdAndConditionType(part.getId(), conditionType)
                .orElseGet(() -> {
                    Inventory inventory = new Inventory();
                    inventory.setPart(part);
                    inventory.setConditionType(conditionType);
                    inventory.setQuantity(0);
                    inventory.setAvgCost(BigDecimal.ZERO);
                    return inventory;
                });
    }

    private Part findPartById(Long partId) {
        return partRepository.findById(partId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Part not found, id: " + partId
                ));
    }

    private InventoryLog findLogById(Long id) {
        return inventoryLogRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Inventory log not found, id: " + id
                ));
    }

    private String normalizeConditionType(String conditionType) {
        if (conditionType == null || conditionType.isBlank()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Condition type is required"
            );
        }

        String normalized = conditionType.trim().toUpperCase();

        if (!normalized.equals("NEW") && !normalized.equals("USED")) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Condition type must be NEW or USED"
            );
        }

        return normalized;
    }

    private String normalizeLogType(String logType) {
        if (logType == null || logType.isBlank()) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Log type is required"
            );
        }

        String normalized = logType.trim().toLowerCase();

        if (!normalized.equals("purchase")
                && !normalized.equals("sale")
                && !normalized.equals("recycle")
                && !normalized.equals("adjust")
                && !normalized.equals("return")) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Log type must be purchase, sale, recycle, adjust, or return"
            );
        }

        return normalized;
    }

    private BigDecimal normalizeMoney(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Unit cost cannot be negative"
            );
        }

        return value;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        return trimmed;
    }

    private InventoryLogResponse toResponse(InventoryLog log) {
        InventoryLogResponse response = new InventoryLogResponse();

        response.setId(log.getId());

        if (log.getInventory() != null) {
            response.setInventoryId(log.getInventory().getId());
        }

        response.setPartId(log.getPart().getId());
        response.setPartCategory(log.getPart().getCategory());
        response.setPartBrand(log.getPart().getBrand());
        response.setPartModel(log.getPart().getModel());

        response.setConditionType(log.getConditionType());
        response.setChangeQty(log.getChangeQty());
        response.setLogType(log.getLogType());
        response.setUnitCost(log.getUnitCost());
        response.setReferenceType(log.getReferenceType());
        response.setReferenceId(log.getReferenceId());
        response.setCreatedBy(log.getCreatedBy());
        response.setNotes(log.getNotes());
        response.setCreatedAt(log.getCreatedAt());

        return response;
    }
}