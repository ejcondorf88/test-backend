package com.example.application.service;

import com.example.application.port.in.UpdateBalanceUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.application.port.out.SaveCreditCardPort;
import com.example.domain.exception.CreditCardNotFoundException;
import com.example.domain.model.CreditCard;
import com.example.domain.model.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Servicio de aplicación que implementa el caso de uso para actualizar el saldo.
 */
@Service
@RequiredArgsConstructor
public class UpdateBalanceService implements UpdateBalanceUseCase {

    private final LoadCreditCardPort loadCreditCardPort;
    private final SaveCreditCardPort saveCreditCardPort;

    @Override
    public CreditCard updateBalance(Long id, BigDecimal amount, OperationType operation) {
        CreditCard creditCard = loadCreditCardPort.loadById(id);
        if (creditCard == null) {
            throw new CreditCardNotFoundException(id);
        }
        
        creditCard.updateBalance(amount, operation);
        return saveCreditCardPort.save(creditCard);
    }
}