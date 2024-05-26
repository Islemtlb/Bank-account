package com.exalt.banking.account.application.exposition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exalt.banking.account.application.model.BankAccountCreationRequest;
import com.exalt.banking.account.application.model.OperationCreationRequest;
import com.exalt.banking.account.application.service.AccountApplicationService;
import com.exalt.banking.account.domain.exceptions.AccountNotFoundException;
import com.exalt.banking.account.domain.exceptions.DepositCeilingExceededException;
import com.exalt.banking.account.domain.exceptions.InsufficientFundsException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class BankAccountController {

    private final AccountApplicationService accountApplicationService;

    public BankAccountController(AccountApplicationService accountApplicationService) {
        this.accountApplicationService = accountApplicationService;

    }

    @PostMapping
    public ResponseEntity<Long> createAccount(
            @RequestBody @Valid BankAccountCreationRequest bankAccountCreationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountApplicationService.createAccount(bankAccountCreationRequest.toBankAccount()));
    }

    @PostMapping("/{id}/operations")
    public ResponseEntity<Void> addOperation(@PathVariable Long id,
            @RequestBody @Valid OperationCreationRequest operationCreationRequest) {
        accountApplicationService.addOperation(id, operationCreationRequest.toOperation());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> handleInsufficientFundsException(InsufficientFundsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DepositCeilingExceededException.class)
    public ResponseEntity<String> handleDepositCeilingExceededException(DepositCeilingExceededException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
