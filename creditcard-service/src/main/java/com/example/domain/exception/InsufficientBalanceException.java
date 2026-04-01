package com.example.domain.exception;

/**
 * Excepción lanzada cuando no hay saldo suficiente en la tarjeta.
 */
public class InsufficientBalanceException extends RuntimeException {
    
    public InsufficientBalanceException(Long cardId, String availableBalance) {
        super("Insufficient balance on credit card with id " + cardId + ". Available: " + availableBalance);
    }
}