package com.zerobase.tabling.annotation;

import com.zerobase.tabling.annotation.validator.ReservationTimeValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReservationTimeValidator.class)
public @interface ReservationTime {
    String message() default "시간 입력 형식 -> 'yyyy-MM-dd HH:mm'";
    Class[] groups() default {};
    Class[] payload() default {};
}
