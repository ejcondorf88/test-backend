package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO para actualizar el saldo de una tarjeta.
 */
public record UpdateBalanceRequest(
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal amount,
        
        @NotNull(message = "El tipo de operación es obligatorio")
        OperationType operation
) {
}