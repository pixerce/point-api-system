package com.point.system.domain.component;

import com.point.system.domain.exception.DomainException;
import com.point.system.domain.valueobject.LifespanUnit;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class LifespanValidatorFactory {

    public static LifespanValidator createLifespanUnit(LifespanUnit unit) {
        if (LifespanUnit.YEAR == unit)
            return new YearLifespanValidator();
        else
            return new DayLifespanValidator();

    }

    public interface LifespanValidator {
        void validate(LocalDateTime startDate, LocalDateTime endDate, Integer lifespan);
    }

    public static class DayLifespanValidator implements LifespanValidator {
        @Override
        public void validate(LocalDateTime startDate, LocalDateTime endDate, Integer lifespan) {
            if (endDate.minusDays(lifespan).isBefore(startDate))
                throw new DomainException("만료 기간 설정 오류");
        }
    }

    public static class YearLifespanValidator implements LifespanValidator {
        @Override
        public void validate(LocalDateTime startDate, LocalDateTime endDate, Integer lifespan) {
            if (endDate.minusYears(lifespan).isBefore(startDate))
                throw new DomainException("만료 기간 설정 오류");
        }
    }
}
