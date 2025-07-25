package com.reliaquest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private List<Employee> data;
    private String status;
}