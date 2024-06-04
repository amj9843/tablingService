package com.zerobase.tabling.annotation;

import com.zerobase.tabling.annotation.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "핸드폰번호 형식 입력 형식 오류";
    Class[] groups() default {};
    Class[] payload() default {};
}
