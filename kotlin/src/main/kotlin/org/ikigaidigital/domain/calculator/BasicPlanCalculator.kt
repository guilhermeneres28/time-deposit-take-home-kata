package org.ikigaidigital.domain.calculator

import org.springframework.stereotype.Component
import java.math.RoundingMode

/**
 * Calculates interest for Basic Plan deposits.
 * 
 * Business Rule: 1% monthly interest rate with a 30-day grace period.
 * 
 * IMPORTANT ASSUMPTION:
 * The requirement states "no interest is applied for the first 30 days".
 * We inferred this means interest starts accruing FROM day 31 onwards (grace period),
 * rather than interest only being paid at maturity date after the grace period.
 *
 * If the actual requirement is to only pay interest at maturity, this logic would need to be adjusted accordingly.
 */
@Component
class BasicPlanCalculator : BaseInterestCalculator() {
    
    override val monthlyInterestRate = 0.01
    override val daysThreshold = 30
    override val daysPerMonth = 30
    override val scale = 4
    override val roundingMode = RoundingMode.HALF_UP
}
