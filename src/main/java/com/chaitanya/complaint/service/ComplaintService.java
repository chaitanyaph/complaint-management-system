package com.chaitanya.complaint.service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaitanya.complaint.dto.ComplaintRequest;
import com.chaitanya.complaint.dto.ComplaintResponse;
import com.chaitanya.complaint.entity.Complaint;
import com.chaitanya.complaint.entity.ComplaintStatus;
import com.chaitanya.complaint.exception.BusinessRuleException;
import com.chaitanya.complaint.exception.NotFoundException;
import com.chaitanya.complaint.repository.ComplaintRepository;

@Service
public class ComplaintService {

    /** Statuses a caller may set directly. ASSIGNED and RESOLVED have their own endpoints (they need extra data). */
    private static final Set<ComplaintStatus> DIRECT_STATUSES = Set.of(ComplaintStatus.IN_PROGRESS, ComplaintStatus.CLOSED);

    private final ComplaintRepository complaints;
    private final PeopleService people;
    private final Clock clock;

    public ComplaintService(ComplaintRepository complaints, PeopleService people, Clock clock) {
        this.complaints = complaints;
        this.people = people;
        this.clock = clock;
    }

    @Transactional
    public ComplaintResponse submit(ComplaintRequest request) {
        Complaint complaint = new Complaint(people.customer(request.customerId()), request.title(),
                request.description(), Instant.now(clock));
        return ComplaintResponse.from(complaints.save(complaint));
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> search(ComplaintStatus status, Long employeeId) {
        return complaints.search(status, employeeId).stream().map(ComplaintResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ComplaintResponse get(Long id) {
        return ComplaintResponse.from(find(id));
    }

    @Transactional
    public ComplaintResponse assign(Long id, Long employeeId) {
        Complaint complaint = find(id);
        requireTransition(complaint, ComplaintStatus.ASSIGNED);
        complaint.setAssignedTo(people.employee(employeeId));
        complaint.setStatus(ComplaintStatus.ASSIGNED);
        return ComplaintResponse.from(complaint);
    }

    @Transactional
    public ComplaintResponse changeStatus(Long id, ComplaintStatus target) {
        if (!DIRECT_STATUSES.contains(target)) {
            throw new BusinessRuleException(
                    "Use the assign endpoint for ASSIGNED and the resolve endpoint for RESOLVED");
        }
        Complaint complaint = find(id);
        requireTransition(complaint, target);
        complaint.setStatus(target);
        return ComplaintResponse.from(complaint);
    }

    @Transactional
    public ComplaintResponse resolve(Long id, String resolution) {
        Complaint complaint = find(id);
        requireTransition(complaint, ComplaintStatus.RESOLVED);
        complaint.resolve(resolution, Instant.now(clock));
        return ComplaintResponse.from(complaint);
    }

    private void requireTransition(Complaint complaint, ComplaintStatus target) {
        if (!complaint.getStatus().canMoveTo(target)) {
            throw new BusinessRuleException(
                    "Cannot change a complaint from " + complaint.getStatus() + " to " + target);
        }
    }

    private Complaint find(Long id) {
        return complaints.findById(id).orElseThrow(() -> new NotFoundException("Complaint " + id + " not found"));
    }
}
