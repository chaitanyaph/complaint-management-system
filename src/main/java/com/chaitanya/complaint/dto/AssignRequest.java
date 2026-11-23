package com.chaitanya.complaint.dto;

import jakarta.validation.constraints.NotNull;

public record AssignRequest(@NotNull Long employeeId) {
}
