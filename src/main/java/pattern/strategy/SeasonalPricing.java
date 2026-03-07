package pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Seasonal Pricing Strategy — applies a 30% surcharge during peak months.
 * Peak season is defined as June–August (summer) and December (holidays).
 * 
 * Why: Revenue maximization during high-demand periods is standard
 * hospitality practice. The Strategy pattern allows this to be activated
 * or deactivated without changing the reservation service.
 * 
 * @author Ocean View Resort Dev Team
 */
public class SeasonalPricing implements PricingStrategy {

    private static final BigDecimal PEAK_MULTIPLIER = new BigDecimal("1.30"); // 30% surcharge
    private static final BigDecimal STANDARD_MULTIPLIER = BigDecimal.ONE;

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkIn, LocalDate checkOut) {
        long totalNights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (totalNights <= 0)
            totalNights = 1;

        BigDecimal total = BigDecimal.ZERO;

        // Iterate each night and apply seasonal multiplier where applicable
        for (long i = 0; i < totalNights; i++) {
            LocalDate night = checkIn.plusDays(i);
            int month = night.getMonthValue();

            BigDecimal multiplier = isPeakMonth(month) ? PEAK_MULTIPLIER : STANDARD_MULTIPLIER;
            total = total.add(basePrice.multiply(multiplier));
        }

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Determines whether a given month falls within peak season.
     *
     * @param month The month number (1–12)
     * @return true if the month is June, July, August, or December
     */
    private boolean isPeakMonth(int month) {
        return month == 6 || month == 7 || month == 8 || month == 12;
    }

    @Override
    public String getStrategyName() {
        return "Seasonal (Peak +30%)";
    }
}
