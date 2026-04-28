package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.PartCreateRequest;
import com.dashuo.pcshop.dto.PartResponse;
import com.dashuo.pcshop.dto.PartUpdateRequest;
import com.dashuo.pcshop.entity.Part;
import com.dashuo.pcshop.exception.BusinessException;
import com.dashuo.pcshop.repository.PartRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    public PartResponse create(PartCreateRequest request) {
        String category = normalizeCategory(request.getCategory());
        String brand = normalizeBrand(request.getBrand());
        String model = normalizeModel(request.getModel());
        String description = normalizeDescription(request.getDescription());

        validateRequiredFields(category, model);

        partRepository.findByCategoryAndBrandAndModel(category, brand, model)
                .ifPresent(existingPart -> {
                    throw new BusinessException(
                            HttpStatus.CONFLICT,
                            "Part already exists: "
                                    + existingPart.getCategory() + " / "
                                    + existingPart.getBrand() + " / "
                                    + existingPart.getModel()
                    );
                });

        Part part = new Part();
        part.setCategory(category);
        part.setBrand(brand);
        part.setModel(model);
        part.setDescription(description);
        part.setActive(true);

        Part saved = partRepository.save(part);
        return toResponse(saved);
    }

    public List<PartResponse> listAll() {
        return partRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PartResponse> listActive() {
        return partRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PartResponse getById(Long id) {
        Part part = findEntityById(id);
        return toResponse(part);
    }

    public PartResponse update(Long id, PartUpdateRequest request) {
        Part existing = findEntityById(id);

        String category = normalizeCategory(request.getCategory());
        String brand = normalizeBrand(request.getBrand());
        String model = normalizeModel(request.getModel());
        String description = normalizeDescription(request.getDescription());

        validateRequiredFields(category, model);

        partRepository.findByCategoryAndBrandAndModel(category, brand, model)
                .ifPresent(duplicatePart -> {
                    if (!duplicatePart.getId().equals(id)) {
                        throw new BusinessException(
                                HttpStatus.CONFLICT,
                                "Part already exists: "
                                        + duplicatePart.getCategory() + " / "
                                        + duplicatePart.getBrand() + " / "
                                        + duplicatePart.getModel()
                        );
                    }
                });

        existing.setCategory(category);
        existing.setBrand(brand);
        existing.setModel(model);
        existing.setDescription(description);

        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }

        Part saved = partRepository.save(existing);
        return toResponse(saved);
    }

    public void deactivate(Long id) {
        Part existing = findEntityById(id);
        existing.setActive(false);
        partRepository.save(existing);
    }

    public List<PartResponse> search(String keyword) {
        return partRepository
                .findByCategoryContainingIgnoreCaseOrBrandContainingIgnoreCaseOrModelContainingIgnoreCase(
                        keyword,
                        keyword,
                        keyword
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Part findEntityById(Long id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Part not found, id: " + id));
    }

    private void validateRequiredFields(String category, String model) {
        if (category == null || category.isBlank()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Part category is required");
        }

        if (model == null || model.isBlank()) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Part model is required");
        }
    }

    private PartResponse toResponse(Part part) {
        PartResponse response = new PartResponse();

        response.setId(part.getId());
        response.setCategory(part.getCategory());
        response.setBrand(part.getBrand());
        response.setModel(part.getModel());
        response.setDescription(part.getDescription());
        response.setActive(part.getActive());
        response.setCreatedAt(part.getCreatedAt());
        response.setUpdatedAt(part.getUpdatedAt());

        return response;
    }

    private String normalizeCategory(String category) {
        if (category == null) {
            return null;
        }

        return category.trim().toUpperCase();
    }

    private String normalizeBrand(String brand) {
        if (brand == null) {
            return null;
        }

        String trimmed = brand.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        return trimmed;
    }

    private String normalizeModel(String model) {
        if (model == null) {
            return null;
        }

        String trimmed = model.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        return trimmed.replaceAll("\\s+", " ");
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }

        String trimmed = description.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        return trimmed;
    }

}