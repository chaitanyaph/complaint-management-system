package com.chaitanya.complaint.dto;

import com.chaitanya.complaint.entity.ComplaintStatus;

import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull ComplaintStatus status) {
}
