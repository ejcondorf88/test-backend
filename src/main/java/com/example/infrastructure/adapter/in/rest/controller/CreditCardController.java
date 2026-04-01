package com.example.infrastructure.adapter.in.rest.controller;

import com.example.application.port.in.CreateCreditCardUseCase;
import com.example.application.port.in.GetCreditCardUseCase;
import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardCreateRequest;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/creditcards")
@RequiredArgsConstructor
public class CreditCardController {

    private final GetCreditCardUseCase getCreditCardUseCase;
    private final CreateCreditCardUseCase createCreditCardUseCase;
    private final CreditCardRestMapper creditCardRestMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CreditCardResponseDTO> getCreditCard(@PathVariable Long id) {
        CreditCard creditCard = getCreditCardUseCase.getCreditCard(id);
        CreditCardResponseDTO responseDTO = creditCardRestMapper.toResponseDTO(creditCard);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<CreditCardResponseDTO> createCreditCard(
            @Valid @RequestBody CreditCardCreateRequest request,
            UriComponentsBuilder uriBuilder) {
        CreditCard created = createCreditCardUseCase.createCreditCard(
            request.cardNumber(),
            request.holderName(),
            request.creditLimit(),
            request.availableBalance(),
            request.status()
        );
        CreditCardResponseDTO response = creditCardRestMapper.toResponseDTO(created);
        URI location = uriBuilder.path("/api/v1/creditcards/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }
}
