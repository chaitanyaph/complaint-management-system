package com.chaitanya.complaint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComplaintRequest(@NotNull Long customerId, @NotBlank String title, @Size(max = 2000) String description) {
}
