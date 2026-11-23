package com.chaitanya.complaint.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.chaitanya.complaint.dto.EmployeeRequest;
import com.chaitanya.complaint.entity.Employee;
import com.chaitanya.complaint.service.PeopleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final PeopleService people;

    public EmployeeController(PeopleService people) {
        this.people = people;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@Valid @RequestBody EmployeeRequest request) {
        return people.addEmployee(request);
    }

    @GetMapping
    public List<Employee> list() {
        return people.employees();
    }

    @GetMapping("/{id}")
    public Employee get(@PathVariable Long id) {
        return people.employee(id);
    }
}
