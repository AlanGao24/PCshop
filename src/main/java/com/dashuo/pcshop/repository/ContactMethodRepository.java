package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.ContactMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactMethodRepository extends JpaRepository<ContactMethod, Long> {

    List<ContactMethod> findByCustomerId(Long customerId);

    List<ContactMethod> findByContactType(String contactType);

    List<ContactMethod> findByContactValueContaining(String keyword);
}