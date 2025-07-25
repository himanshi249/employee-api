package com.reliaquest.api.controller.impl;

import com.reliaquest.api.controller.IEmployeeController;
import com.reliaquest.api.dto.Employee;
import com.reliaquest.api.dto.CreateEmployeeRequest;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee-api")
public class EmployeeController implements IEmployeeController<Employee, CreateEmployeeRequest> {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        return ResponseEntity.ok(employeeService.searchEmployeesByName(searchString));
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalary());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestPaidEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(CreateEmployeeRequest employeeInput) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }
}