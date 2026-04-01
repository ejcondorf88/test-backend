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

    /**
     * Actualiza el saldo de la tarjeta según el tipo de operación.
     * 
     * @param amount   Monto a operar
     * @param operation Tipo de operación (CONSUMO o PAGO)
     * @throws IllegalStateException si la tarjeta está bloqueada
     * @throws IllegalArgumentException si el monto es negativo o cero
     * @throws IllegalStateException si no hay saldo suficiente para CONSUMO
     * @throws IllegalStateException si el PAGO excede el límite de crédito
     */
    public void updateBalance(BigDecimal amount, OperationType operation) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (this.isBlocked()) {
            throw new IllegalStateException("No se puede operar una tarjeta bloqueada");
        }

        switch (operation) {
            case CONSUMO -> {
                if (this.availableBalance.compareTo(amount) < 0) {
                    throw new IllegalStateException("Saldo insuficiente. Disponible: " + this.availableBalance);
                }
                this.availableBalance = this.availableBalance.subtract(amount);
            }
            case PAGO -> {
                BigDecimal newBalance = this.availableBalance.add(amount);
                if (newBalance.compareTo(this.creditLimit) > 0) {
                    throw new IllegalStateException("El pago excedería el límite de crédito. Límite: " + this.creditLimit);
                }
                this.availableBalance = newBalance;
            }
        }

        this.updatedAt = LocalDateTime.now();
    }
}
