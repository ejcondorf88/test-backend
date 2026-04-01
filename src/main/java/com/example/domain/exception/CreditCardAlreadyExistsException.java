package com.example.domain.exception;

public class CreditCardAlreadyExistsException extends RuntimeException {

    public CreditCardAlreadyExistsException(String cardNumber) {
        super("Credit card with number " + cardNumber + " already exists");
    }
}
