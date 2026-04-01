package com.example.infrastructure.adapter.in.rest.mapper;

import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;

import java.util.List;

/**
 * Mapper para convertir DTOs de la API REST al modelo de dominio.
 */
public class CreditCardRestMapper {

    public CreditCard toDomain(CreditCardResponseDTO dto) {
        if (dto == null) {
            return null;
        }
        return CreditCard.builder()
                .id(dto.id())
                .cardNumber(dto.cardNumber())
                .holderName(dto.holderName())
                .creditLimit(dto.creditLimit())
                .availableBalance(dto.availableBalance())
                .status(dto.status())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .build();
    }

    public List<CreditCard> toDomainList(List<CreditCardResponseDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toDomain)
                .toList();
    }
}