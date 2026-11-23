package com.chaitanya.complaint.dto;

import java.time.Instant;

import com.chaitanya.complaint.entity.Complaint;
import com.chaitanya.complaint.entity.ComplaintStatus;

public record ComplaintResponse(Long id, Long customerId, String customerName, String title, String description,
        ComplaintStatus status, Long assignedToId, String assignedToName, String resolution, Instant createdAt,
        Instant resolvedAt) {

    public static ComplaintResponse from(Complaint c) {
        return new ComplaintResponse(c.getId(), c.getCustomer().getId(), c.getCustomer().getName(), c.getTitle(),
                c.getDescription(), c.getStatus(),
                c.getAssignedTo() == null ? null : c.getAssignedTo().getId(),
                c.getAssignedTo() == null ? null : c.getAssignedTo().getName(),
                c.getResolution(), c.getCreatedAt(), c.getResolvedAt());
    }
}
