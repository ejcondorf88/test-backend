package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.CreditCardStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para tarjetas de crédito.
 * Se utiliza para la comunicación interna dentro del servicio.
 */
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