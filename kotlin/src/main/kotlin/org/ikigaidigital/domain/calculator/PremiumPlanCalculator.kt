package org.ikigaidigital.domain.calculator

import org.springframework.stereotype.Component
import java.math.RoundingMode

/**
 * Calculates interest for Premium Plan deposits.
 * 
 * Business Rule: 5% monthly interest rate with a 45-day grace period.
 * 
 * IMPORTANT ASSUMPTION:
 * The requirement states "interest starts after 45 days".
 * We inferred this means interest starts accruing FROM day 46 onwards (grace period),
 * rather than interest only being paid at maturity date after the grace period.
 *
 * If the actual requirement is to only pay interest at maturity, this logic would need to be adjusted accordingly.
 */
@Component
class PremiumPlanCalculator : BaseInterestCalculator() {
    
    override val monthlyInterestRate = 0.05
    override val daysThreshold = 45
    override val daysPerMonth = 30
    override val scale = 4
    override val roundingMode = RoundingMode.HALF_UP
}
