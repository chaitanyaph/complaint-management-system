package com.chaitanya.complaint.exception;

/** Thrown when a request is well-formed but breaks a business rule. */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
