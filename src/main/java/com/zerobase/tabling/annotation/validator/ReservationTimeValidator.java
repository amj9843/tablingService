package com.zerobase.tabling.annotation.validator;

import com.zerobase.tabling.annotation.ReservationTime;
import com.zerobase.tabling.component.TimeConverter;
import com.zerobase.tabling.exception.impl.ReservationTimeisNotValidException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ReservationTimeValidator implements ConstraintValidator<ReservationTime, String> {
    private final TimeConverter timeConverter;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false;

        try {
            LocalDateTime checkTime = this.timeConverter.stringToLocalDateTime(s);
            if (checkTime.isAfter(LocalDateTime.now().plusHours(1))) return true;
        } catch (Exception e) {
            throw new ReservationTimeisNotValidException();
        }

        return false;
    }
}