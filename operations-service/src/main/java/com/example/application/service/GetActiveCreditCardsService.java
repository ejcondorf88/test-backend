package com.example.application.service;

import com.example.application.port.in.GetActiveCreditCardsUseCase;
import com.example.application.port.out.CreditCardQueryRepository;
import com.example.domain.model.CreditCard;

import java.util.List;

/**
 * Servicio de aplicación que implementa el caso de uso para obtener tarjetas activas.
 * Coordina el flujo entre el puerto de entrada y el puerto de salida.
 */
public class GetActiveCreditCardsService implements GetActiveCreditCardsUseCase {

    private final CreditCardQueryRepository creditCardQueryRepository;

    public GetActiveCreditCardsService(CreditCardQueryRepository creditCardQueryRepository) {
        this.creditCardQueryRepository = creditCardQueryRepository;
    }

    @Override
    public List<CreditCard> getActiveCreditCards() {
        return creditCardQueryRepository.findAllActiveCreditCards();
    }
}