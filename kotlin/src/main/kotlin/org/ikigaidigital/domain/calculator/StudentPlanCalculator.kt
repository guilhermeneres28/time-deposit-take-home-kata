package org.ikigaidigital.domain.calculator

import java.math.RoundingMode

/**
 * Calculates interest for Student Plan deposits.
 * 
 * Business Rule: 3% monthly interest rate with a 30-day grace period.
 * Special condition: No interest after 1 year (365 days).
 * 
 * IMPORTANT ASSUMPTION:
 * The requirement states "no interest is applied for the first 30 days".
 * We inferred this means interest starts accruing FROM day 31 onwards (grace period),
 * rather than interest only being paid at maturity date after the grace period.
 *
 * If the actual requirement is to only pay interest at maturity, this logic would need to be adjusted accordingly.
 */
class StudentPlanCalculator : BaseInterestCalculator() {
    
    override val monthlyInterestRate = 0.03
    override val daysThreshold = 30
    override val daysPerMonth = 30
    override val scale = 4
    override val roundingMode = RoundingMode.HALF_UP
    
    override fun calculateMonths(days: Int): Int {
        if (days <= daysThreshold) return 0
        if (days > YEAR_LIMIT) return 0 // TODO: Make sure this condition is correct, because the business rule states "no interest after 1 year (365 days)", needs to return at least the 365 days interest and stop calculate after 365 days
        return ((days - daysThreshold - 1) / daysPerMonth) + 1
    }
    
    companion object {
        private const val YEAR_LIMIT = 365
    }
}
