package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.CustomerCreateRequest;
import com.dashuo.pcshop.dto.CustomerResponse;
import com.dashuo.pcshop.dto.CustomerUpdateRequest;
import com.dashuo.pcshop.entity.Customer;
import com.dashuo.pcshop.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse create(CustomerCreateRequest request) {
        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setChannel(request.getChannel());
        customer.setCustomerType(request.getCustomerType());
        customer.setNotes(request.getNotes());
        customer.setTotalSpent(BigDecimal.ZERO);

        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    public List<CustomerResponse> listAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse getById(Long id) {
        Customer customer = findEntityById(id);
        return toResponse(customer);
    }

    public CustomerResponse update(Long id, CustomerUpdateRequest request) {
        Customer existingCustomer = findEntityById(id);

        existingCustomer.setName(request.getName());
        existingCustomer.setChannel(request.getChannel());
        existingCustomer.setCustomerType(request.getCustomerType());
        existingCustomer.setNotes(request.getNotes());

        Customer savedCustomer = customerRepository.save(existingCustomer);

        return toResponse(savedCustomer);
    }

    public void delete(Long id) {
        Customer existingCustomer = findEntityById(id);
        customerRepository.delete(existingCustomer);
    }

    public List<CustomerResponse> searchByName(String name) {
        return customerRepository.findByNameContaining(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Customer findEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found, id: " + id));
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setChannel(customer.getChannel());
        response.setCustomerType(customer.getCustomerType());
        response.setNotes(customer.getNotes());
        response.setTotalSpent(customer.getTotalSpent());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }
}