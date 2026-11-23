package com.chaitanya.complaint.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = ComplaintStatus.OPEN;

    @ManyToOne
    private Employee assignedTo;

    @Column(length = 2000)
    private String resolution;

    private Instant createdAt;
    private Instant resolvedAt;

    protected Complaint() {
    }

    public Complaint(Customer customer, String title, String description, Instant createdAt) {
        this.customer = customer;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Customer getCustomer() { return customer; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public ComplaintStatus getStatus() { return status; }
    public Employee getAssignedTo() { return assignedTo; }
    public String getResolution() { return resolution; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getResolvedAt() { return resolvedAt; }

    public void setStatus(ComplaintStatus status) { this.status = status; }
    public void setAssignedTo(Employee assignedTo) { this.assignedTo = assignedTo; }

    public void resolve(String resolution, Instant at) {
        this.status = ComplaintStatus.RESOLVED;
        this.resolution = resolution;
        this.resolvedAt = at;
    }
}
