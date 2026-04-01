package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.CreditCardStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreditCardCreateRequest(
        @NotBlank
        @Size(min = 13, max = 19)
        String cardNumber,

        @NotBlank
        @Size(max = 150)
        String holderName,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal creditLimit,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal availableBalance,

        @NotNull
        CreditCardStatus status
) {
}
