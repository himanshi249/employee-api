package com.reliaquest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SingleEmployeeResponse {
    private Employee data;
    private String status;
}