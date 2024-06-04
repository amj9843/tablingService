package com.zerobase.tabling.component;

import com.zerobase.tabling.exception.impl.ReservationTimeisNotValid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TimeConverter {
    private final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    public LocalDateTime stringToLocalDateTime(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

        try {
            return LocalDateTime.parse(string, formatter);
        } catch (Exception e) {
            throw new ReservationTimeisNotValid();
        }
    }
}
