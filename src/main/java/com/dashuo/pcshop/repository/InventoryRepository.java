package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByPartId(Long partId);

    Optional<Inventory> findByPartIdAndConditionType(Long partId, String conditionType);

    List<Inventory> findByConditionType(String conditionType);

    List<Inventory> findByQuantityLessThanEqual(Integer quantity);
}