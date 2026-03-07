package pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Weekend Pricing Strategy — applies a 15% surcharge on Friday and Saturday
 * nights.
 * Weekday nights remain at standard rate.
 * 
 * @author Ocean View Resort Dev Team
 */
public class WeekendPricing implements PricingStrategy {

    private static final BigDecimal WEEKEND_MULTIPLIER = new BigDecimal("1.15"); // 15% surcharge
    private static final BigDecimal WEEKDAY_MULTIPLIER = BigDecimal.ONE;

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkIn, LocalDate checkOut) {
        long totalNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (totalNights <= 0)
            totalNights = 1;

        BigDecimal total = BigDecimal.ZERO;

        // Iterate each night and check if it falls on a weekend
        for (long i = 0; i < totalNights; i++) {
            LocalDate night = checkIn.plusDays(i);
            DayOfWeek day = night.getDayOfWeek();

            BigDecimal multiplier = isWeekendNight(day) ? WEEKEND_MULTIPLIER : WEEKDAY_MULTIPLIER;
            total = total.add(basePrice.multiply(multiplier));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * A "weekend night" is defined as Friday or Saturday.
     * Why: Guests checking in on Friday or Saturday represent peak demand.
     *
     * @param day The day of the week
     * @return true if Friday or Saturday
     */
    private boolean isWeekendNight(DayOfWeek day) {
        return day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
    }

    @Override
    public String getStrategyName() {
        return "Weekend (+15% Fri/Sat)";
    }
}
