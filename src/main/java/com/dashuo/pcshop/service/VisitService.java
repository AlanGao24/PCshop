package com.dashuo.pcshop.service;

import com.dashuo.pcshop.dto.VisitCreateRequest;
import com.dashuo.pcshop.dto.VisitResponse;
import com.dashuo.pcshop.dto.VisitUpdateRequest;
import com.dashuo.pcshop.entity.Customer;
import com.dashuo.pcshop.entity.Visit;
import com.dashuo.pcshop.exception.BusinessException;
import com.dashuo.pcshop.repository.CustomerRepository;
import com.dashuo.pcshop.repository.VisitRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final CustomerRepository customerRepository;

    public VisitService(
            VisitRepository visitRepository,
            CustomerRepository customerRepository
    ) {
        this.visitRepository = visitRepository;
        this.customerRepository = customerRepository;
    }

    public VisitResponse create(VisitCreateRequest request) {
        Visit visit = new Visit();

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = findCustomerById(request.getCustomerId());
        }

        visit.setCustomer(customer);
        visit.setEmployeeId(request.getEmployeeId());
        visit.setVisitTime(request.getVisitTime() == null ? LocalDateTime.now() : request.getVisitTime());
        visit.setVisitType(normalizeText(request.getVisitType()));
        visit.setPeopleCount(normalizePeopleCount(request.getPeopleCount()));
        visit.setScenario(normalizeText(request.getScenario()));
        visit.setPurpose(normalizeText(request.getPurpose()));
        visit.setHasClearDemand(Boolean.TRUE.equals(request.getHasClearDemand()));
        visit.setNotes(normalizeText(request.getNotes()));

        Visit saved = visitRepository.save(visit);
        return toResponse(saved);
    }

    public List<VisitResponse> listAll() {
        return visitRepository.findAllByOrderByVisitTimeDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VisitResponse getById(Long id) {
        Visit visit = findVisitById(id);
        return toResponse(visit);
    }

    public VisitResponse update(Long id, VisitUpdateRequest request) {
        Visit visit = findVisitById(id);

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = findCustomerById(request.getCustomerId());
        }

        visit.setCustomer(customer);
        visit.setEmployeeId(request.getEmployeeId());
        visit.setVisitTime(request.getVisitTime() == null ? visit.getVisitTime() : request.getVisitTime());
        visit.setVisitType(normalizeText(request.getVisitType()));
        visit.setPeopleCount(normalizePeopleCount(request.getPeopleCount()));
        visit.setScenario(normalizeText(request.getScenario()));
        visit.setPurpose(normalizeText(request.getPurpose()));
        visit.setHasClearDemand(Boolean.TRUE.equals(request.getHasClearDemand()));
        visit.setNotes(normalizeText(request.getNotes()));

        Visit saved = visitRepository.save(visit);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Visit visit = findVisitById(id);
        visitRepository.delete(visit);
    }

    public List<VisitResponse> listByCustomerId(Long customerId) {
        findCustomerById(customerId);

        return visitRepository.findByCustomerIdOrderByVisitTimeDesc(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VisitResponse> listByEmployeeId(Long employeeId) {
        return visitRepository.findByEmployeeIdOrderByVisitTimeDesc(employeeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VisitResponse> listByScenario(String scenario) {
        String normalizedScenario = normalizeText(scenario);

        if (normalizedScenario == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Scenario is required"
            );
        }

        return visitRepository.findByScenarioOrderByVisitTimeDesc(normalizedScenario)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VisitResponse> listByPurpose(String purpose) {
        String normalizedPurpose = normalizeText(purpose);

        if (normalizedPurpose == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Purpose is required"
            );
        }

        return visitRepository.findByPurposeOrderByVisitTimeDesc(normalizedPurpose)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VisitResponse> listByDate(LocalDate date) {
        if (date == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Date is required"
            );
        }

        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.plusDays(1).atStartOfDay();

        return visitRepository.findByVisitTimeBetweenOrderByVisitTimeDesc(startTime, endTime)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VisitResponse> listByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "Start date and end date are required"
            );
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date"
            );
        }

        LocalDateTime startTime = startDate.atStartOfDay();
        LocalDateTime endTime = endDate.plusDays(1).atStartOfDay();

        return visitRepository.findByVisitTimeBetweenOrderByVisitTimeDesc(startTime, endTime)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Visit findVisitById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Visit not found, id: " + id
                ));
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found, id: " + customerId
                ));
    }

    private Integer normalizePeopleCount(Integer peopleCount) {
        if (peopleCount == null) {
            return 1;
        }

        if (peopleCount <= 0) {
            throw new BusinessException(
                    HttpStatus.BAD_REQUEST,
                    "People count must be greater than 0"
            );
        }

        return peopleCount;
    }

    private String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        if (trimmed.isBlank()) {
            return null;
        }

        return trimmed;
    }

    private VisitResponse toResponse(Visit visit) {
        VisitResponse response = new VisitResponse();

        response.setId(visit.getId());

        if (visit.getCustomer() != null) {
            response.setCustomerId(visit.getCustomer().getId());
            response.setCustomerName(visit.getCustomer().getName());
        }

        response.setEmployeeId(visit.getEmployeeId());
        response.setVisitTime(visit.getVisitTime());
        response.setVisitType(visit.getVisitType());
        response.setPeopleCount(visit.getPeopleCount());
        response.setScenario(visit.getScenario());
        response.setPurpose(visit.getPurpose());
        response.setHasClearDemand(visit.getHasClearDemand());
        response.setNotes(visit.getNotes());
        response.setCreatedAt(visit.getCreatedAt());
        response.setUpdatedAt(visit.getUpdatedAt());

        return response;
    }
}