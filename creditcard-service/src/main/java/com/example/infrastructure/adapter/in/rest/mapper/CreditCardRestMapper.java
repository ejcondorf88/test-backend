package com.example.infrastructure.adapter.in.rest.mapper;

import com.example.domain.model.CreditCard;
import com.example.infrastructure.adapter.in.rest.dto.CreditCardResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CreditCardRestMapper {

    @Mapping(source = "creditCard", target = "cardNumber", qualifiedByName = "maskedCardNumber")
    CreditCardResponseDTO toResponseDTO(CreditCard creditCard);

    @Named("maskedCardNumber")
    default String mapMaskedCardNumber(CreditCard creditCard) {
        return creditCard.getMaskedNumber();
    }
}
