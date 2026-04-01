package com.example.domain.exception;

public class CreditCardBlockedException extends RuntimeException {
    public CreditCardBlockedException(Long id) {
        super("Cannot operate on blocked credit card with id " + id);
    }
}
