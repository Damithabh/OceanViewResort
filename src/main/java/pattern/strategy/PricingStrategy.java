package pattern.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Strategy Pattern — Pricing Strategy Interface.
 * Defines a contract for interchangeable pricing algorithms.
 * 
 * Why Strategy: Different pricing rules (standard, seasonal, weekend)
 * can be swapped at runtime without modifying the core booking logic.
 * This follows the Open/Closed Principle — open for extension, closed
 * for modification.
 * 
 * // Learned from https://refactoring.guru/design-patterns/strategy
 * 
 * @author Ocean View Resort Dev Team
 */
public interface PricingStrategy {

    /**
     * Calculates the total price for a stay based on the strategy's rules.
     *
     * @param basePrice The nightly rate of the room
     * @param checkIn   The check-in date
     * @param checkOut  The check-out date
     * @return The computed total price
     */
    BigDecimal calculatePrice(BigDecimal basePrice, LocalDate checkIn, LocalDate checkOut);

    /**
     * Returns a human-readable name for this pricing strategy.
     *
     * @return The strategy name (e.g., "Standard", "Seasonal")
     */
    String getStrategyName();
}
