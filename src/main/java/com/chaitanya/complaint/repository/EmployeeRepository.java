package com.chaitanya.complaint.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chaitanya.complaint.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
