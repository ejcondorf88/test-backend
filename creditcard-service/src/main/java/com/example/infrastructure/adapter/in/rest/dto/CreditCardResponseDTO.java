package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.CreditCardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardResponseDTO(
        Long id,
        String cardNumber,
        String holderName,
        BigDecimal creditLimit,
        BigDecimal availableBalance,
        CreditCardStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
