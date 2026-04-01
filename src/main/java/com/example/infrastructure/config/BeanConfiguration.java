package com.example.infrastructure.config;

import com.example.application.port.in.GetCreditCardUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.application.service.GetCreditCardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public GetCreditCardUseCase getCreditCardUseCase(LoadCreditCardPort loadCreditCardPort) {
        return new GetCreditCardService(loadCreditCardPort);
    }
}
