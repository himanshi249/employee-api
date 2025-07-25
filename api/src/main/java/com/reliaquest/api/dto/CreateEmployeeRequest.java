package com.reliaquest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateEmployeeRequest {
    private String name;
    private int salary;
    private int age;
    private String title;
}