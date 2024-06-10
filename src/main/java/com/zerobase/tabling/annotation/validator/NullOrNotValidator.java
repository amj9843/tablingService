package com.zerobase.tabling.annotation.validator;

import com.zerobase.tabling.annotation.NullOrNot;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotValidator implements ConstraintValidator<NullOrNot, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; //아예 입력값이 없거나
        } else if (value.isBlank()) {
            return false;
        }

        return true;
    }
}
