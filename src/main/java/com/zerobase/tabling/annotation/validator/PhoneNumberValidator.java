package com.zerobase.tabling.annotation.validator;

import com.zerobase.tabling.annotation.PhoneNumber;
import com.zerobase.tabling.exception.impl.PhoneNumberisNotValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isEmpty()) {
            return true; //아예 입력값이 빈값이거나(핸드폰이 없는 경우)
        } else if (s.matches("01(?:0|1|[6-9])-\\d{3,4}-\\d{4}$")) {
            return true; //010-1234-1234 형식이거나
        } else if (s.matches("01(?:0|1|[6-9])\\d{3,4}\\d{4}$")) {
            return true; // 01012341234 형식이거나
        }

        throw new PhoneNumberisNotValid();
    }
}
