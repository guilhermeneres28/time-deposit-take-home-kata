package org.ikigaidigital.domain.calculator

import org.springframework.stereotype.Component
import java.math.RoundingMode

/**
 * Calculates interest for Student Plan deposits.
 * 
 * Business Rule: 3% monthly interest rate with a 30-day grace period.
 * Special condition: No interest after 1 year (365 days).
 * 
 * IMPORTANT ASSUMPTIONS (inferred from requirements):
 * 1. "No interest is applied for the first 30 days" means interest starts 
 *    accruing FROM day 31 onwards (grace period), rather than interest only 
 *    being paid at maturity date after the grace period.
 *
 * 2. "No interest after 1 year (365 days)" was interpreted as: interest 
 *    continues to accrue UP TO 365 days, then STOPS calculating after that.
 *    This means a deposit at 365 days receives full interest for those 365 days,
 *    but a deposit at 366+ days receives the same interest as at day 365 (capped).
 *    The requirement was ambiguous, so we inferred this behavior as the most
 *    logical interpretation for a student savings plan with a time limit benefit.
 */
@Component
class StudentPlanCalculator : BaseInterestCalculator() {
    
    override val monthlyInterestRate = 0.03
    override val daysThreshold = 30
    override val daysPerMonth = 30
    override val scale = 4
    override val roundingMode = RoundingMode.HALF_UP
    
    override fun calculateMonths(days: Int): Int {
        if (days <= daysThreshold) return 0
        val effectiveDays = if (days > YEAR_LIMIT) YEAR_LIMIT else days
        return ((effectiveDays - daysThreshold - 1) / daysPerMonth) + 1
    }
    
    companion object {
        private const val YEAR_LIMIT = 365
    }
}
