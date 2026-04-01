package com.example.application.port.in;

import com.example.infrastructure.adapter.in.rest.dto.OperationResponse;

/**
 * Puerto de entrada para procesar operaciones (consumo/pago) en tarjetas.
 */
public interface ProcessOperationUseCase {

    /**
     * Procesa una operación de consumo o pago en una tarjeta.
     * 
     * @param cardId    ID de la tarjeta
     * @param amount   Monto a operar
     * @param operation Tipo de operación (CONSUMO o PAGO)
     * @return Response con los detalles de la operación procesada
     */
    OperationResponse processOperation(Long cardId, java.math.BigDecimal amount, 
            com.example.domain.model.OperationType operation);
}