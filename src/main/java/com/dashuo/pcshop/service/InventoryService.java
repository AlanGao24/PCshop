package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.InventoryCreateRequest;
import com.dashuo.pcshop.dto.InventoryResponse;
import com.dashuo.pcshop.dto.InventoryUpdateRequest;
import com.dashuo.pcshop.entity.Inventory;
import com.dashuo.pcshop.entity.Part;
import com.dashuo.pcshop.exception.BusinessException;
import com.dashuo.pcshop.repository.InventoryRepository;
import com.dashuo.pcshop.repository.PartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final PartRepository partRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            PartRepository partRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.partRepository = partRepository;
    }

    public InventoryResponse create(InventoryCreateRequest request) {
        validateCreateRequest(request);

        Long partId = request.getPartId();
        String conditionType = normalizeConditionType(request.getConditionType());

        Part part = findPartById(partId);

        inventoryRepository.findByPartIdAndConditionType(partId, conditionType)
                .ifPresent(existing -> {
                    throw new BusinessException(
                            HttpStatus.CONFLICT,
                            "Inventory already exists for partId "
                                    + partId
                                    + " with condition "
                                    + conditionType
                    );
                });

        Inventory inventory = new Inventory();
        inventory.setPart(part);
        inventory.setConditionType(conditionType);
        inventory.setQuantity(request.getQuantity());
        inventory.setAvgCost(normalizeMoney(request.getAvgCost()));

        Inventory saved = inventoryRepository.save(inventory);
        return toResponse(saved);
    }

    public List<InventoryResponse> listAll() {
        return inventoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InventoryResponse getById(Long id) {
        Inventory inventory = findInventoryById(id);
        return toResponse(inventory);
    }

    public List<InventoryResponse> listByPartId(Long partId) {
        findPartById(partId);

        return inventoryRepository.findByPartId(partId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventoryResponse> listByConditionType(String conditionType) {
        String normalizedConditionType = normalizeConditionType(conditionType);

        return inventoryRepository.findByConditionType(normalizedConditionType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<InventoryResponse> listLowStock(Integer threshold) {
        if (threshold == null || threshold < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Low stock threshold must be greater than or equal to 0"
            );
        }

        return inventoryRepository.findByQuantityLessThanEqual(threshold)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public InventoryResponse update(Long id, InventoryUpdateRequest request) {
        Inventory inventory = findInventoryById(id);

        if (request.getQuantity() == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Inventory quantity is required"
            );
        }

        if (request.getQuantity() < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Inventory quantity cannot be negative"
            );
        }

        BigDecimal avgCost = normalizeMoney(request.getAvgCost());

        inventory.setQuantity(request.getQuantity());
        inventory.setAvgCost(avgCost);

        Inventory saved = inventoryRepository.save(inventory);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Inventory inventory = findInventoryById(id);
        inventoryRepository.delete(inventory);
    }

    private void validateCreateRequest(InventoryCreateRequest request) {
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
                    "Inventory quantity is required"
            );
        }

        if (request.getQuantity() < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Inventory quantity cannot be negative"
            );
        }

        normalizeMoney(request.getAvgCost());
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

    private BigDecimal normalizeMoney(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }

        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Average cost cannot be negative"
            );
        }

        return value;
    }

    private Part findPartById(Long partId) {
        return partRepository.findById(partId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Part not found, id: " + partId
                ));
    }

    private Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Inventory not found, id: " + id
                ));
    }

    private InventoryResponse toResponse(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();

        response.setId(inventory.getId());
        response.setPartId(inventory.getPart().getId());
        response.setPartCategory(inventory.getPart().getCategory());
        response.setPartBrand(inventory.getPart().getBrand());
        response.setPartModel(inventory.getPart().getModel());
        response.setConditionType(inventory.getConditionType());
        response.setQuantity(inventory.getQuantity());
        response.setAvgCost(inventory.getAvgCost());
        response.setLastUpdatedAt(inventory.getLastUpdatedAt());

        return response;
    }
}