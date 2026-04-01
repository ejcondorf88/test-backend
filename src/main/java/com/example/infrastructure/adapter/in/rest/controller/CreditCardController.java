package com.example.infrastructure.adapter.in.rest.controller;

import com.example.application.port.in.GetCreditCardUseCase;
import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/creditcards")
@RequiredArgsConstructor
public class CreditCardController {

    private final GetCreditCardUseCase getCreditCardUseCase;
    private final CreditCardRestMapper creditCardRestMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> getCreditCard(@PathVariable Long id) {
        CreditCard creditCard = getCreditCardUseCase.getCreditCard(id);
        CreditCardResponseDTO responseDTO = creditCardRestMapper.toResponseDTO(creditCard);
        return ResponseEntity.ok(responseDTO);
    }
}
