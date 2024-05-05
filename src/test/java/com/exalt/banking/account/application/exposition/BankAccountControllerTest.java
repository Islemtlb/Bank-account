package com.exalt.banking.account.application.exposition;

import com.exalt.banking.account.application.service.AccountApplicationService;
import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

        @Mock
        private AccountApplicationService accountApplicationService;

        @InjectMocks
        private BankAccountController bankAccountController;

        @Test
        void handleAccountNotFoundException_should_return_not_found_response() {
                // Given
                AccountNotFoundException exception = new AccountNotFoundException("Account not found");

                // When
                ResponseEntity<String> responseEntity = bankAccountController.handleAccountNotFoundException(exception);

                // Then
                assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                assertEquals("Account not found", responseEntity.getBody());
        }

        @Test
        void handleInsufficientFundsException_should_return_bad_request_response() {
                // Given
                InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds");

                // When
                ResponseEntity<String> responseEntity = bankAccountController
                                .handleInsufficientFundsException(exception);

                // Then
                assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
                assertEquals("Insufficient funds", responseEntity.getBody());
        }
}
