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

import com.chaitanya.complaint.dto.CustomerRequest;
import com.chaitanya.complaint.entity.Customer;
import com.chaitanya.complaint.service.PeopleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final PeopleService people;

    public CustomerController(PeopleService people) {
        this.people = people;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody CustomerRequest request) {
        return people.addCustomer(request);
    }

    @GetMapping
    public List<Customer> list() {
        return people.customers();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return people.customer(id);
    }
}
