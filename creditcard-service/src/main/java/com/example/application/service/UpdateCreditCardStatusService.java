package com.example.application.service;

import com.example.application.port.in.UpdateCreditCardStatusUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.application.port.out.SaveCreditCardPort;
import com.example.domain.exception.CreditCardNotFoundException;
import com.example.domain.model.CreditCard;
import com.example.domain.model.CreditCardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCreditCardStatusService implements UpdateCreditCardStatusUseCase {

    private final LoadCreditCardPort loadCreditCardPort;
    private final SaveCreditCardPort saveCreditCardPort;

    @Override
    public CreditCard updateStatus(Long id, CreditCardStatus status) {
        CreditCard creditCard = loadCreditCardPort.loadById(id);
        if (creditCard == null) {
            throw new CreditCardNotFoundException(id);
        }
        creditCard.updateStatus(status);
        return saveCreditCardPort.save(creditCard);
    }
}
