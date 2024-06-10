package com.zerobase.tabling.annotation;

import com.zerobase.tabling.annotation.validator.NullOrNotValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NullOrNotValidator.class)
public @interface NullOrNot {
    String message() default "빈칸, 공백으로 이루어진 문자열이 아니어야 합니다. 단, null은 허용합니다.";
    Class[] groups() default {};
    Class[] payload() default {};
}
