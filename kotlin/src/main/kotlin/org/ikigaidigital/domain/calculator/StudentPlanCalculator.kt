package org.ikigaidigital.domain.calculator

import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.TimeDeposit
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

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
class StudentPlanCalculator : InterestCalculator {
    
    override fun calculate(deposit: TimeDeposit): DomainResult<Money> {
        val monthlyRate = MONTHLY_INTEREST_RATE
        val months = calculateMonths(deposit.days)
        
        if (months == 0) {
            return DomainResult.success(Money.ZERO)
        }
        
        val principal = deposit.balance.amount
        val compoundFactor = (1.0 + monthlyRate).pow(months)
        val finalAmount = principal.multiply(BigDecimal.valueOf(compoundFactor))
        val interest = finalAmount.subtract(principal)
        
        return DomainResult.success(Money.of(interest.setScale(SCALE, ROUNDING_MODE)))
    }
    
    private fun calculateMonths(days: Int): Int {
        if (days <= DAYS_THRESHOLD) return 0
        if (days > YEAR_LIMIT) return 0
        return ((days - DAYS_THRESHOLD - 1) / DAYS_PER_MONTH) + 1
    }
    
    companion object {
        private const val MONTHLY_INTEREST_RATE = 0.03
        private const val DAYS_THRESHOLD = 30
        private const val DAYS_PER_MONTH = 30
        private const val YEAR_LIMIT = 365
        private const val SCALE = 4
        private val ROUNDING_MODE = RoundingMode.HALF_UP
    }
}
