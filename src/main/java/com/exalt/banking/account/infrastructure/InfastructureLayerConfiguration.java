package com.exalt.banking.account.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.exalt.banking.account.domain.ports.AccountRepository;
import com.exalt.banking.account.domain.ports.OperationRepository;
import com.exalt.banking.account.domain.service.AccountService;
import com.exalt.banking.account.domain.service.AccountServiceImpl;
import com.exalt.banking.account.infrastructure.db.jpa.repository.AccountRepositoryAdaptor;
import com.exalt.banking.account.infrastructure.db.jpa.repository.JpaAccountRepository;
import com.exalt.banking.account.infrastructure.db.jpa.repository.JpaOperationRepository;
import com.exalt.banking.account.infrastructure.db.jpa.repository.OperationRepositoryAdaptor;

@Configuration
public class InfastructureLayerConfiguration {

    @Bean
    AccountService accountService(AccountRepository accountRepository) {
        return new AccountServiceImpl(accountRepository);
    }

    @Bean
    AccountRepository accountPort(JpaAccountRepository accountRepository) {
        return new AccountRepositoryAdaptor(accountRepository);
    }

    @Bean
    OperationRepository operationPort(JpaOperationRepository operationRepository,
            JpaAccountRepository accountRepository) {
        return new OperationRepositoryAdaptor(operationRepository, accountRepository);
    }

    @Bean
    OperationRepositoryAdaptor operationRepositoryAdaptor(JpaOperationRepository jpaOperationRepository,
            JpaAccountRepository jpaAccountRepository) {
        return new OperationRepositoryAdaptor(jpaOperationRepository, jpaAccountRepository);

    }

}
