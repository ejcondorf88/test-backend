package com.example.application.port.in;

import com.example.domain.exception.CreditCardAlreadyExistsException;
import com.example.domain.model.CreditCard;
import com.example.domain.model.CreditCardStatus;

import java.math.BigDecimal;

public interface CreateCreditCardUseCase {

    CreditCard createCreditCard(String cardNumber,
                                String holderName,
                                BigDecimal creditLimit,
                                BigDecimal availableBalance,
                                CreditCardStatus status) throws CreditCardAlreadyExistsException;
}
