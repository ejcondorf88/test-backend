package com.example.application.port.in;

import com.example.domain.model.CreditCard;

import java.util.List;

/**
 * Puerto de entrada para obtener tarjetas de crédito activas.
 * Este es el puerto que define la operación desde la perspectiva del dominio.
 */
public interface GetActiveCreditCardsUseCase {

    /**
     * Obtiene todas las tarjetas de crédito que están en estado ACTIVA.
     * 
     * @return Lista de tarjetas de crédito activas
     */
    List<CreditCard> getActiveCreditCards();
}