package com.chaitanya.complaint.entity;

import java.util.Set;

public enum ComplaintStatus {
    OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CLOSED;

    /** Allowed next statuses. A resolved complaint can be closed, or reopened if the fix did not hold. */
    public Set<ComplaintStatus> next() {
        return switch (this) {
            case OPEN -> Set.of(ASSIGNED);
            case ASSIGNED -> Set.of(ASSIGNED, IN_PROGRESS);
            case IN_PROGRESS -> Set.of(RESOLVED);
            case RESOLVED -> Set.of(CLOSED, IN_PROGRESS);
            case CLOSED -> Set.of();
        };
    }

    public boolean canMoveTo(ComplaintStatus target) {
        return next().contains(target);
    }
}
