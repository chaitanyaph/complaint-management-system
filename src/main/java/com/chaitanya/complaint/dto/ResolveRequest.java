package com.chaitanya.complaint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResolveRequest(@NotBlank @Size(max = 2000) String resolution) {
}
