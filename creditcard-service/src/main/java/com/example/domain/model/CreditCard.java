package com.example.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    private Long id;
    private String cardNumber;
    private String holderName;
    private BigDecimal creditLimit;
    private BigDecimal availableBalance;
    private CreditCardStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getMaskedNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return "****" + cardNumber.substring(cardNumber.length() - 4);
    }

    public void updateStatus(CreditCardStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isBlocked() {
        return this.status == CreditCardStatus.BLOQUEADA;
    }
}
