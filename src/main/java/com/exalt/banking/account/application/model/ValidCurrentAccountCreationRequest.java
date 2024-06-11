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
@Constraint(validatedBy = CurrentAccountCreationRequestValidator.class)
public @interface ValidCurrentAccountCreationRequest {
    String message() default "Demande de création de compte courant invalide";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class CurrentAccountCreationRequestValidator implements
        ConstraintValidator<ValidCurrentAccountCreationRequest, BankAccountCreationRequest> {

    @Override
    public boolean isValid(BankAccountCreationRequest request, ConstraintValidatorContext context) {
        if (request.accountType() != BankAccountType.CURRENT) {
            return true;
        }

        if (request.overdraftLimit() == null || request.overdraftLimit().compareTo(BigDecimal.ZERO) <= 0) {
            String message = "Le plafond de découvert est requis et doit être supérieur à 0 pour créer un compte COURANT";
            addConstraintViolation(context, "overdraftLimit", message);
            return false;
        }

        BigDecimal balance = request.balance();

        if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0
                && balance.abs().compareTo(request.overdraftLimit()) > 0) {
            String message = "La balance négative ne doit pas dépasser la limite de découvert";
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
