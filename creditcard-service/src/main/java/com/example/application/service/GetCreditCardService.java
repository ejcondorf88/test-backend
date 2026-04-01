package com.example.application.service;

import com.example.application.port.in.GetCreditCardUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.domain.exception.CreditCardNotFoundException;
import com.example.domain.model.CreditCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCreditCardService implements GetCreditCardUseCase {

    private final LoadCreditCardPort loadCreditCardPort;

    @Override
    public CreditCard getCreditCard(Long id) throws CreditCardNotFoundException {
        CreditCard creditCard = loadCreditCardPort.loadById(id);
        if (creditCard == null) {
            throw new CreditCardNotFoundException(id);
        }
        return creditCard;
    }
}
