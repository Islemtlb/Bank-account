package com.exalt.banking.account.application.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

import com.exalt.banking.account.domain.model.BankAccountType;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SavingsAccountCreationRequestValidator.class)
public @interface ValidSavingsAccountCreationRequest {
    String message() default "Demande de création de compte d'épargne invalide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class SavingsAccountCreationRequestValidator implements
        ConstraintValidator<ValidSavingsAccountCreationRequest, BankAccountCreationRequest> {

    @Override
    public boolean isValid(BankAccountCreationRequest request, ConstraintValidatorContext context) {

        if (request.accountType() != BankAccountType.SAVINGS) {
            return true;
        }

        if (request.savingsDepositLimit() == null || request.savingsDepositLimit().compareTo(BigDecimal.ZERO) < 0) {
            String message = "Le plafond de dépôt est requis et doit être supérieur à 0 pour créer un compte d'épargne";
            addConstraintViolation(context, "savingsDepositLimit", message);
            return false;
        }

        BigDecimal balance = request.balance();

        if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
            String message = "La balance ne peut pas être négative pour un compte d'épargne";
            addConstraintViolation(context, "balance", message);
            return false;
        }

        if (balance != null && balance.compareTo(request.savingsDepositLimit()) > 0) {
            String message = "La balance ne doit pas dépasser le plafond de dépôt";
            addConstraintViolation(context, "balance", message);
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String property, String message) {
        context.disableDefaultConstraintViolation();
        ConstraintValidatorContext.ConstraintViolationBuilder builder = context
                .buildConstraintViolationWithTemplate(message);
        if (builder != null) {
            builder.addPropertyNode(property).addConstraintViolation();
        }
    }
}
