package com.dashuo.pcshop.repository;

import com.dashuo.pcshop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContaining(String name);

    List<Customer> findByChannel(String channel);

    List<Customer> findByCustomerType(String customerType);
}