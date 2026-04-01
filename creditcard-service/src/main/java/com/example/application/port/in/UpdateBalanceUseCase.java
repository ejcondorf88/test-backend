package com.example.application.port.in;

import com.example.domain.model.CreditCard;
import com.example.domain.model.OperationType;

import java.math.BigDecimal;

/**
 * Caso de uso para actualizar el saldo de una tarjeta de crédito.
 */
public interface UpdateBalanceUseCase {

    /**
     * Actualiza el saldo de una tarjeta según el tipo de operación.
     * 
     * @param id        ID de la tarjeta
     * @param amount    Monto a operar
     * @param operation Tipo de operación (CONSUMO o PAGO)
     * @return La tarjeta con el saldo actualizado
     */
    CreditCard updateBalance(Long id, BigDecimal amount, OperationType operation);
}