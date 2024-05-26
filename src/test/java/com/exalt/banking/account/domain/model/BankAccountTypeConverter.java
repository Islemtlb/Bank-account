package com.exalt.banking.account.domain.model;

import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.converter.ArgumentConversionException;

public class BankAccountTypeConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (source == null) {
            throw new ArgumentConversionException("Source is null");
        }
        if (targetType != BankAccountType.class) {
            throw new ArgumentConversionException("Conversion target type is not BankAccountType");
        }
        try {
            return BankAccountType.valueOf(source.toString());
        } catch (IllegalArgumentException e) {
            throw new ArgumentConversionException("Failed to convert " + source + " to BankAccountType", e);
        }
    }
}
