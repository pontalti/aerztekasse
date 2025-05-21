package com.demo.aerztekasse.validation;

import com.demo.aerztekasse.annotation.StartBeforeEnd;
import com.demo.aerztekasse.records.OpenIntervalRecord;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Slf4j
public class StartBeforeEndValidator
    implements ConstraintValidator<StartBeforeEnd, OpenIntervalRecord> {

    @Override
    public boolean isValid(OpenIntervalRecord rec, ConstraintValidatorContext ctx) {
        if (rec == null) {
        	return true;
        }

        try {
            LocalTime start = LocalTime.parse(rec.start());
            LocalTime end   = LocalTime.parse(rec.end());

            if (start.equals(end)) {
                return false;
            }

            if (end.equals(LocalTime.MIDNIGHT)) {
                return true;
            }
            
            return start.isBefore(end);
        } catch (DateTimeParseException ex) {
        	log.error("The ValidTime annotation will validate");
        	return true;
        }
    }
}