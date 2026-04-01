package com.example.infrastructure.adapter.in.rest.mapper;

import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;

import java.util.List;

/**
 * Mapper para convertir modelos de dominio a DTOs de respuesta REST.
 */
public class CreditCardResponseMapper {

    public CreditCardResponseDTO toDto(CreditCard creditCard) {
        if (creditCard == null) {
            return null;
        }
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getMaskedNumber(),
                creditCard.getHolderName(),
                creditCard.getCreditLimit(),
                creditCard.getAvailableBalance(),
                creditCard.getStatus(),
                creditCard.getCreatedAt(),
                creditCard.getUpdatedAt()
        );
    }

    public List<CreditCardResponseDTO> toDtoList(List<CreditCard> creditCards) {
        if (creditCards == null) {
            return List.of();
        }
        return creditCards.stream()
                .map(this::toDto)
                .toList();
    }
}