package pattern.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Standard Pricing Strategy — default rate calculation.
 * Simply multiplies the base price by the number of nights.
 * 
 * @author Ocean View Resort Dev Team
 */
public class StandardPricing implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0)
            nights = 1;
        return basePrice.multiply(new BigDecimal(nights));
    }

    @Override
    public String getStrategyName() {
        return "Standard";
    }
}
