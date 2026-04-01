package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Request DTO para procesar una operación (consumo o pago).
 */
public record OperationRequest(
        @NotNull(message = "El ID de la tarjeta es obligatorio")
        Long cardId,
        
        @NotNull(message = "El monto es obligatorio")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal amount,
        
        @NotNull(message = "El tipo de operación es obligatorio")
        OperationType operation
) {
}