package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByCustomerIdOrderByVisitTimeDesc(Long customerId);

    List<Visit> findByEmployeeIdOrderByVisitTimeDesc(Long employeeId);

    List<Visit> findByScenarioOrderByVisitTimeDesc(String scenario);

    List<Visit> findByPurposeOrderByVisitTimeDesc(String purpose);

    List<Visit> findByVisitTimeBetweenOrderByVisitTimeDesc(
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    List<Visit> findAllByOrderByVisitTimeDesc();
}