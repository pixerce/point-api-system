package com.point.system.domain.component;

import com.point.system.domain.valueobject.LifespanUnit;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class LifespanCalculatorFactory {

    public static LifespanCalculator createLifespanUnit(LifespanUnit lifespanUnit) {
        if (LifespanUnit.YEAR == lifespanUnit)
            return new YearCalculator();
        else
            return new DayCalculator();
    }

    public interface LifespanCalculator {
        LocalDateTime calculate(LocalDateTime dateTime, Integer lifespan);
    }

    public static class DayCalculator implements LifespanCalculator {
        @Override
        public LocalDateTime calculate(LocalDateTime dateTime, Integer lifespan) {
            return dateTime.plusDays(lifespan);
        }
    }

    public static class YearCalculator implements LifespanCalculator {
        @Override
        public LocalDateTime calculate(LocalDateTime dateTime, Integer lifespan) {
            return dateTime.plusYears(lifespan);
        }
    }
}
