package com.example.application.port.in;

import com.example.domain.exception.CreditCardNotFoundException;
import com.example.domain.model.CreditCard;

import java.util.List;

public interface GetCreditCardUseCase {

    CreditCard getCreditCard(Long id) throws CreditCardNotFoundException;
    
    List<CreditCard> getAllCreditCards();
}
