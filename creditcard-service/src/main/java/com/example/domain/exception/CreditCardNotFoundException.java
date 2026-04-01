package com.example.domain.exception;

public class CreditCardNotFoundException extends RuntimeException {

    public CreditCardNotFoundException(Long id) {
        super("Credit card with id " + id + " not found");
    }
}
