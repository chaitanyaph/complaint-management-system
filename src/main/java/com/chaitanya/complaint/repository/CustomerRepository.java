package com.chaitanya.complaint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chaitanya.complaint.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
