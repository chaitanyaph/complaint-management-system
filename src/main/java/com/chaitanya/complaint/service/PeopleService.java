package com.chaitanya.complaint.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chaitanya.complaint.dto.CustomerRequest;
import com.chaitanya.complaint.dto.EmployeeRequest;
import com.chaitanya.complaint.entity.Customer;
import com.chaitanya.complaint.entity.Employee;
import com.chaitanya.complaint.exception.NotFoundException;
import com.chaitanya.complaint.repository.CustomerRepository;
import com.chaitanya.complaint.repository.EmployeeRepository;

/** Customers and employees: the two kinds of people the complaint workflow connects. */
@Service
public class PeopleService {

    private final CustomerRepository customers;
    private final EmployeeRepository employees;

    public PeopleService(CustomerRepository customers, EmployeeRepository employees) {
        this.customers = customers;
        this.employees = employees;
    }

    @Transactional
    public Customer addCustomer(CustomerRequest request) {
        return customers.saveAndFlush(new Customer(request.name(), request.email()));
    }

    @Transactional(readOnly = true)
    public List<Customer> customers() {
        return customers.findAll();
    }

    @Transactional(readOnly = true)
    public Customer customer(Long id) {
        return customers.findById(id).orElseThrow(() -> new NotFoundException("Customer " + id + " not found"));
    }

    @Transactional
    public Employee addEmployee(EmployeeRequest request) {
        return employees.saveAndFlush(new Employee(request.name(), request.email(), request.department()));
    }

    @Transactional(readOnly = true)
    public List<Employee> employees() {
        return employees.findAll();
    }

    @Transactional(readOnly = true)
    public Employee employee(Long id) {
        return employees.findById(id).orElseThrow(() -> new NotFoundException("Employee " + id + " not found"));
    }
}
