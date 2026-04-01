package com.example.infrastructure.adapter.in.rest.dto;

import com.example.domain.model.CreditCardStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCreditCardStatusRequest(
        @NotNull
        CreditCardStatus status
) {
}
