package com.example.domain.model;

/**
 * Tipo de operación para actualizar el saldo de la tarjeta.
 */
public enum OperationType {
    /**
     * Consumo - deduce saldo (el cliente paga algo)
     */
    CONSUMO,
    
    /**
     * Pago - aumenta saldo (el cliente paga lo que debe)
     */
    PAGO
}