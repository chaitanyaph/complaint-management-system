package com.chaitanya.complaint.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chaitanya.complaint.dto.AssignRequest;
import com.chaitanya.complaint.dto.ComplaintRequest;
import com.chaitanya.complaint.dto.ComplaintResponse;
import com.chaitanya.complaint.dto.ResolveRequest;
import com.chaitanya.complaint.dto.StatusRequest;
import com.chaitanya.complaint.entity.ComplaintStatus;
import com.chaitanya.complaint.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComplaintResponse submit(@Valid @RequestBody ComplaintRequest request) {
        return service.submit(request);
    }

    /** Optional filters: status and employeeId (the employee the complaint is assigned to). */
    @GetMapping
    public List<ComplaintResponse> search(@RequestParam(required = false) ComplaintStatus status,
            @RequestParam(required = false) Long employeeId) {
        return service.search(status, employeeId);
    }

    @GetMapping("/{id}")
    public ComplaintResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}/assign")
    public ComplaintResponse assign(@PathVariable Long id, @Valid @RequestBody AssignRequest request) {
        return service.assign(id, request.employeeId());
    }

    @PatchMapping("/{id}/status")
    public ComplaintResponse changeStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return service.changeStatus(id, request.status());
    }

    @PutMapping("/{id}/resolve")
    public ComplaintResponse resolve(@PathVariable Long id, @Valid @RequestBody ResolveRequest request) {
        return service.resolve(id, request.resolution());
    }
}
