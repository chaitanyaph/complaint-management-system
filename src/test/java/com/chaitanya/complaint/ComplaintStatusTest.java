package com.chaitanya.complaint;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.chaitanya.complaint.entity.ComplaintStatus;

class ComplaintStatusTest {

    @Test
    void theNormalLifecycleIsAllowed() {
        assertThat(ComplaintStatus.OPEN.canMoveTo(ComplaintStatus.ASSIGNED)).isTrue();
        assertThat(ComplaintStatus.ASSIGNED.canMoveTo(ComplaintStatus.IN_PROGRESS)).isTrue();
        assertThat(ComplaintStatus.IN_PROGRESS.canMoveTo(ComplaintStatus.RESOLVED)).isTrue();
        assertThat(ComplaintStatus.RESOLVED.canMoveTo(ComplaintStatus.CLOSED)).isTrue();
    }

    @Test
    void stepsCannotBeSkippedAndClosedIsFinal() {
        assertThat(ComplaintStatus.OPEN.canMoveTo(ComplaintStatus.CLOSED)).isFalse();
        assertThat(ComplaintStatus.OPEN.canMoveTo(ComplaintStatus.RESOLVED)).isFalse();
        assertThat(ComplaintStatus.CLOSED.next()).isEmpty();
    }

    @Test
    void aResolvedComplaintCanBeReopenedAndAssignedOnesReassigned() {
        assertThat(ComplaintStatus.RESOLVED.canMoveTo(ComplaintStatus.IN_PROGRESS)).isTrue();
        assertThat(ComplaintStatus.ASSIGNED.canMoveTo(ComplaintStatus.ASSIGNED)).isTrue();
    }
}
