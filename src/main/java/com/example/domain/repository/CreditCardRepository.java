package com.example.domain.repository;

import com.example.domain.model.CreditCard;

import java.util.Optional;

public interface CreditCardRepository {

    Optional<CreditCard> findById(Long id);
}
