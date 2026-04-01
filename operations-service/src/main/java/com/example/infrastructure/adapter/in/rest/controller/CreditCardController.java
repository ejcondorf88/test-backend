package com.example.infrastructure.adapter.in.rest.controller;

import com.example.application.port.in.GetActiveCreditCardsUseCase;
import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para operaciones relacionadas con tarjetas de crédito.
 * Expone los endpoints para consultar tarjetas desde el mundo exterior.
 */
@RestController
@RequestMapping("/api/v1/credit-cards")
public class CreditCardController {

    private final GetActiveCreditCardsUseCase getActiveCreditCardsUseCase;
    private final CreditCardResponseMapper mapper;

    public CreditCardController(
            GetActiveCreditCardsUseCase getActiveCreditCardsUseCase,
            CreditCardResponseMapper mapper) {
        this.getActiveCreditCardsUseCase = getActiveCreditCardsUseCase;
        this.mapper = mapper;
    }

    /**
     * GET /api/v1/credit-cards/active
     * Obtiene todas las tarjetas de crédito que están en estado ACTIVA.
     * 
     * @return Lista de tarjetas activas
     */
    @GetMapping("/active")
    public ResponseEntity<List<CreditCardResponseDTO>> getActiveCreditCards() {
        List<CreditCard> activeCards = getActiveCreditCardsUseCase.getActiveCreditCards();
        List<CreditCardResponseDTO> response = mapper.toDtoList(activeCards);
        return ResponseEntity.ok(response);
    }
}