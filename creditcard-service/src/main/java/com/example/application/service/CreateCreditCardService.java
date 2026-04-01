package com.example.application.service;

import com.example.application.port.in.CreateCreditCardUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.application.port.out.SaveCreditCardPort;
import com.example.domain.exception.CreditCardAlreadyExistsException;
import com.example.domain.model.CreditCard;
import com.example.domain.model.CreditCardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CreateCreditCardService implements CreateCreditCardUseCase {

    private final SaveCreditCardPort saveCreditCardPort;
    private final LoadCreditCardPort loadCreditCardPort;

    @Override
    public CreditCard createCreditCard(String cardNumber,
                                       String holderName,
                                       BigDecimal creditLimit,
                                       BigDecimal availableBalance,
                                       CreditCardStatus status) {
        // Verificar si ya existe
        if (loadCreditCardPort.existsByCardNumber(cardNumber)) {
            throw new CreditCardAlreadyExistsException(cardNumber);
        }

        // Crear entidad
        CreditCard creditCard = CreditCard.builder()
            .cardNumber(cardNumber)
            .holderName(holderName)
            .creditLimit(creditLimit)
            .availableBalance(availableBalance)
            .status(status)
            .build();

        return saveCreditCardPort.save(creditCard);
    }
}
