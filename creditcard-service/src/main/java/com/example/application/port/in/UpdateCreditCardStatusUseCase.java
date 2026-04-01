package com.example.application.port.in;

import com.example.domain.model.CreditCard;
import com.example.domain.model.CreditCardStatus;

public interface UpdateCreditCardStatusUseCase {

    CreditCard updateStatus(Long id, CreditCardStatus status);
}
