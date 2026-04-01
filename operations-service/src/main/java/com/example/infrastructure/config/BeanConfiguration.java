package com.example.infrastructure.config;

import com.example.application.port.in.GetActiveCreditCardsUseCase;
import com.example.application.port.in.ProcessOperationUseCase;
import com.example.application.port.out.CreditCardQueryRepository;
import com.example.application.service.GetActiveCreditCardsService;
import com.example.application.service.ProcessOperationService;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardResponseMapper;
import com.example.infrastructure.adapter.in.rest.mapper.CreditCardRestMapper;
import com.example.infrastructure.adapter.out.creditcard.CreditCardClient;
import com.example.infrastructure.adapter.out.persistence.mapper.CreditCardPersistenceMapper;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        // Usar Apache HttpClient que soporta PATCH nativamente
        var httpClient = HttpClients.createDefault();
        
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        
        return new RestTemplate(factory);
    }

    @Bean
    public CreditCardRestMapper creditCardRestMapper() {
        return new CreditCardRestMapper();
    }

    @Bean
    public CreditCardClient creditCardClient(RestTemplate restTemplate) {
        return new CreditCardClient(restTemplate, "http://localhost:9000");
    }

    @Bean
    public CreditCardPersistenceMapper creditCardPersistenceMapper() {
        return new CreditCardPersistenceMapper();
    }

    // CreditCardJpaRepository y CreditCardQueryRepositoryImpl se auto-detectan por Spring Data JPA

    @Bean
    public GetActiveCreditCardsUseCase getActiveCreditCardsUseCase(
            CreditCardQueryRepository creditCardQueryRepository) {
        return new GetActiveCreditCardsService(creditCardQueryRepository);
    }

    @Bean
    public CreditCardResponseMapper creditCardResponseMapper() {
        return new CreditCardResponseMapper();
    }

    @Bean
    public ProcessOperationUseCase processOperationUseCase(
            CreditCardClient creditCardClient,
            CreditCardRestMapper creditCardRestMapper) {
        return new ProcessOperationService(creditCardClient, creditCardRestMapper);
    }
}