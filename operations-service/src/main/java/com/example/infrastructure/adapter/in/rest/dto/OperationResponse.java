package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.CreditCardStatus;
import com.example.domain.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO para una operación procesada.
 */
public record OperationResponse(
        Long cardId,
        BigDecimal previousBalance,
        BigDecimal newBalance,
        BigDecimal amount,
        OperationType operation,
        LocalDateTime processedAt
) {
}