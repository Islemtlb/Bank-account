package com.exalt.banking.account.application.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.BigDecimal;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBalanceValidator.class)
public @interface ValidBalance {
    String message() default "La balance négative ne doit pas dépasser la limite de découvert";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ValidBalanceValidator implements ConstraintValidator<ValidBalance, BankAccountCreationRequest> {

    @Override
    public boolean isValid(BankAccountCreationRequest request, ConstraintValidatorContext context) {
        if (request.balance() == null || request.overdraftLimit() == null) {
            return true;
        }

        BigDecimal balance = request.balance();
        BigDecimal overdraftLimit = request.overdraftLimit();

        if (balance.compareTo(BigDecimal.ZERO) < 0 && balance.abs().compareTo(overdraftLimit) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("balance")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
