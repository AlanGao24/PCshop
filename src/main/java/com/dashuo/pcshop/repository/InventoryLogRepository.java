package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {

    List<InventoryLog> findByPartIdOrderByCreatedAtDesc(Long partId);

    List<InventoryLog> findByInventoryIdOrderByCreatedAtDesc(Long inventoryId);

    List<InventoryLog> findByLogTypeOrderByCreatedAtDesc(String logType);

    List<InventoryLog> findByConditionTypeOrderByCreatedAtDesc(String conditionType);

    List<InventoryLog> findAllByOrderByCreatedAtDesc();
}