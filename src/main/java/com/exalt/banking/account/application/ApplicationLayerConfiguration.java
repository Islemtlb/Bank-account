package com.exalt.banking.account.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exalt.banking.account.application.service.AccountApplicationService;
import com.exalt.banking.account.domain.service.AccountService;

@Configuration
public class ApplicationLayerConfiguration {

    @Bean
    AccountApplicationService accountApplicationService(AccountService accountService) {
        return new AccountApplicationService(accountService);
    }

}