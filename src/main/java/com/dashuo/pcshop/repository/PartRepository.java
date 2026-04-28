package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, Long> {

    List<Part> findByActiveTrue();

    List<Part> findByCategoryContainingIgnoreCaseOrBrandContainingIgnoreCaseOrModelContainingIgnoreCase(
            String category,
            String brand,
            String model
    );

    Optional<Part> findByCategoryAndBrandAndModel(String category, String brand, String model);
}