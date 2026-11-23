package com.chaitanya.complaint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(@NotBlank String name, @NotBlank @Email String email, String department) {
}
