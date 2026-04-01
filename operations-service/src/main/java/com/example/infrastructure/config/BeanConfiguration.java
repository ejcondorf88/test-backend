package com.example.infrastructure.config;

import com.example.application.port.in.GetActiveCreditCardsUseCase;
import com.example.application.port.out.CreditCardQueryRepository;
import com.example.application.service.GetActiveCreditCardsService;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardResponseMapper;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardRestMapper;
import com.example.infrastructure.adapter.out.creditcard.CreditCardClient;
import com.example.infrastructure.adapter.out.creditcard.CreditCardQueryRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CreditCardRestMapper creditCardRestMapper() {
        return new CreditCardRestMapper();
    }

    @Bean
    public CreditCardClient creditCardClient(RestTemplate restTemplate) {
        return new CreditCardClient(restTemplate, "http://localhost:8080");
    }

    @Bean
    public CreditCardQueryRepository creditCardQueryRepository(
            CreditCardClient creditCardClient,
            CreditCardRestMapper mapper) {
        return new CreditCardQueryRepositoryImpl(creditCardClient, mapper);
    }

    @Bean
    public GetActiveCreditCardsUseCase getActiveCreditCardsUseCase(
            CreditCardQueryRepository creditCardQueryRepository) {
        return new GetActiveCreditCardsService(creditCardQueryRepository);
    }

    @Bean
    public CreditCardResponseMapper creditCardResponseMapper() {
        return new CreditCardResponseMapper();
    }
}