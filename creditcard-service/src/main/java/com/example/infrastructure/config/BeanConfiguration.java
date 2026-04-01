package com.example.infrastructure.config;

import com.example.application.port.in.CreateCreditCardUseCase;
import com.example.application.port.in.GetCreditCardUseCase;
import com.example.application.port.in.UpdateBalanceUseCase;
import com.example.application.port.in.UpdateCreditCardStatusUseCase;
import com.example.application.port.out.LoadCreditCardPort;
import com.example.application.port.out.SaveCreditCardPort;
import com.example.application.service.CreateCreditCardService;
import com.example.application.service.GetCreditCardService;
import com.example.application.service.UpdateBalanceService;
import com.example.application.service.UpdateCreditCardStatusService;
import com.example.infrastructure.adapter.out.persistence.CreditCardPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public LoadCreditCardPort loadCreditCardPort(CreditCardPersistenceAdapter adapter) {
        return adapter;
    }

    @Bean
    public SaveCreditCardPort saveCreditCardPort(CreditCardPersistenceAdapter adapter) {
        return adapter;
    }

    @Bean
    public GetCreditCardUseCase getCreditCardUseCase(LoadCreditCardPort loadCreditCardPort) {
        return new GetCreditCardService(loadCreditCardPort);
    }

    @Bean
    public CreateCreditCardUseCase createCreditCardUseCase(
            SaveCreditCardPort saveCreditCardPort,
            LoadCreditCardPort loadCreditCardPort) {
        return new CreateCreditCardService(saveCreditCardPort, loadCreditCardPort);
    }

    @Bean
    public UpdateCreditCardStatusUseCase updateCreditCardStatusUseCase(
            LoadCreditCardPort loadCreditCardPort,
            SaveCreditCardPort saveCreditCardPort) {
        return new UpdateCreditCardStatusService(loadCreditCardPort, saveCreditCardPort);
    }

    @Bean
    public UpdateBalanceUseCase updateBalanceUseCase(
            LoadCreditCardPort loadCreditCardPort,
            SaveCreditCardPort saveCreditCardPort) {
        return new UpdateBalanceService(loadCreditCardPort, saveCreditCardPort);
    }
}
