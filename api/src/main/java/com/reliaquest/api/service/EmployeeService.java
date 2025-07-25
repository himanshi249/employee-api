package com.reliaquest.api.service;

import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.EmployeeResponse;
import com.reliaquest.api.dto.SingleEmployeeResponse;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.dto.DeleteEmployeeRequest;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService {
    private final RestTemplate restTemplate;

    @Value("${employee.base-url}")
    private String baseUrl;

    public EmployeeService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public List<Employee> getAllEmployees() {
        ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(baseUrl, EmployeeResponse.class);
        if (HttpStatus.OK == response.getStatusCode() && response.getBody() != null) {
            log.info("All employees fetched successfully!");
            return response.getBody().getData();
        } else {
            log.error("Failed to fetch employees. Status: {}", response.getStatusCode());
            return Collections.emptyList();
        }
    }

    public Employee getEmployeeById(String id) {
        ResponseEntity<SingleEmployeeResponse> response = restTemplate.getForEntity(baseUrl + "/" + id, SingleEmployeeResponse.class);
        if (HttpStatus.OK == response.getStatusCode() && response.getBody() != null) {
            log.info("Employee with id: {} fetched successfully!", id);
            return response.getBody().getData();
        } else {
            log.error("Failed to fetch employee with id: {}. Status: {}", id, response.getStatusCode());
            return null;
        }
    }

    public List<Employee> searchEmployeesByName(String fragment) {
        return getAllEmployees().stream()
                .filter(e -> e.getEmployee_name().toLowerCase().contains(fragment.toLowerCase()))
                .collect(Collectors.toList());
    }

    public Integer getHighestSalary() {
        return getAllEmployees().stream()
                .mapToInt(Employee::getEmployee_salary)
                .max()
                .orElse(0);
    }

    public List<String> getTop10HighestPaidEmployeeNames() {
        return getAllEmployees().stream()
                .sorted(Comparator.comparingInt(Employee::getEmployee_salary).reversed())
                .limit(10)
                .map(Employee::getEmployee_name)
                .collect(Collectors.toList());
    }

    public Employee createEmployee(CreateEmployeeRequest req) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateEmployeeRequest> request = new HttpEntity<>(req, headers);
        ResponseEntity<SingleEmployeeResponse> response = restTemplate.postForEntity(baseUrl, request, SingleEmployeeResponse.class);
        if (HttpStatus.OK == response.getStatusCode() && response.getBody() != null) {
            log.info("Employee created successfully!");
            return response.getBody().getData();
        } else {
            log.error("Failed to create employee. Status: {}", response.getStatusCode());
            return null;
        }
    }

    public String deleteEmployeeById(String id) {
        Employee employee = searchEmployeesById(id);
        DeleteEmployeeRequest deleteEmployeeRequest = new DeleteEmployeeRequest(employee.getEmployee_name());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DeleteEmployeeRequest> requestEntity = new HttpEntity<>(deleteEmployeeRequest, headers);

        restTemplate.exchange(
                baseUrl,
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );
        return id;
    }

    private Employee searchEmployeesById(String id) {
        return getAllEmployees().stream()
                .filter(e -> e.getId().toString().equals(id))
                .findFirst().orElse(null);
    }
}