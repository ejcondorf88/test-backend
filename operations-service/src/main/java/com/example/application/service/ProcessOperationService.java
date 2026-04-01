package com.example.application.service;

import com.example.application.port.in.ProcessOperationUseCase;
import com.example.domain.model.CreditCard;
import com.example.domain.model.OperationType;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;
import com.example.infrastructure.adapter.in.rest.dto.OperationResponse;
import com.example.infrastructure.adapter.out.creditcard.CreditCardClient;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Servicio que procesa operaciones de consumo/pago en tarjetas.
 */
@Service
@RequiredArgsConstructor
public class ProcessOperationService implements ProcessOperationUseCase {

    private final CreditCardClient creditCardClient;
    private final CreditCardRestMapper mapper;

    @Override
    public OperationResponse processOperation(Long cardId, BigDecimal amount, OperationType operation) {
        // Obtener saldo anterior
        CreditCardResponseDTO cardBefore = creditCardClient.getById(cardId);
        
        BigDecimal previousBalance = cardBefore.availableBalance();
        
        // Procesar la operación (llamar al endpoint de creditcard-service)
        CreditCardResponseDTO cardAfter = creditCardClient.updateBalance(cardId, amount, operation);
        
        // Construir respuesta
        return new OperationResponse(
                cardId,
                previousBalance,
                cardAfter.availableBalance(),
                amount,
                operation,
                LocalDateTime.now()
        );
    }
}