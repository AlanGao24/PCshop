package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.ContactMethodCreateRequest;
import com.dashuo.pcshop.dto.ContactMethodResponse;
import com.dashuo.pcshop.dto.ContactMethodUpdateRequest;
import com.dashuo.pcshop.entity.ContactMethod;
import com.dashuo.pcshop.entity.Customer;
import com.dashuo.pcshop.repository.ContactMethodRepository;
import com.dashuo.pcshop.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMethodService {

    private final ContactMethodRepository contactMethodRepository;
    private final CustomerRepository customerRepository;

    public ContactMethodService(
            ContactMethodRepository contactMethodRepository,
            CustomerRepository customerRepository
    ) {
        this.contactMethodRepository = contactMethodRepository;
        this.customerRepository = customerRepository;
    }

    public ContactMethodResponse create(Long customerId, ContactMethodCreateRequest request) {
        Customer customer = findCustomerById(customerId);

        ContactMethod contactMethod = new ContactMethod();
        contactMethod.setCustomer(customer);
        contactMethod.setContactType(request.getContactType());
        contactMethod.setContactValue(request.getContactValue());
        contactMethod.setPrimaryContact(Boolean.TRUE.equals(request.getPrimaryContact()));

        ContactMethod saved = contactMethodRepository.save(contactMethod);

        return toResponse(saved);
    }

    public List<ContactMethodResponse> listByCustomerId(Long customerId) {
        findCustomerById(customerId);

        return contactMethodRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ContactMethodResponse getById(Long id) {
        ContactMethod contactMethod = findContactMethodById(id);
        return toResponse(contactMethod);
    }

    public ContactMethodResponse update(Long id, ContactMethodUpdateRequest request) {
        ContactMethod existing = findContactMethodById(id);

        existing.setContactType(request.getContactType());
        existing.setContactValue(request.getContactValue());
        existing.setPrimaryContact(Boolean.TRUE.equals(request.getPrimaryContact()));

        ContactMethod saved = contactMethodRepository.save(existing);

        return toResponse(saved);
    }

    public void delete(Long id) {
        ContactMethod existing = findContactMethodById(id);
        contactMethodRepository.delete(existing);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found, id: " + customerId));
    }

    private ContactMethod findContactMethodById(Long id) {
        return contactMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact method not found, id: " + id));
    }

    private ContactMethodResponse toResponse(ContactMethod contactMethod) {
        ContactMethodResponse response = new ContactMethodResponse();

        response.setId(contactMethod.getId());
        response.setCustomerId(contactMethod.getCustomer().getId());
        response.setContactType(contactMethod.getContactType());
        response.setContactValue(contactMethod.getContactValue());
        response.setPrimaryContact(contactMethod.getPrimaryContact());
        response.setCreatedAt(contactMethod.getCreatedAt());
        response.setUpdatedAt(contactMethod.getUpdatedAt());

        return response;
    }
}