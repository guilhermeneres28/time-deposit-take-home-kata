package org.ikigaidigital

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Legacy TimeDepositCalculator - Preserved for backward compatibility.
 * 
 * NOTE: This class has been refactored into a new strategy-based architecture
 * located in the domain.calculator package. The new implementation fixes a critical
 * issue where interest was being calculated incorrectly as simple interest instead
 * of compound interest, leading to accumulated calculation errors over time.
 * 
 * The new architecture uses:
 * - Strategy Pattern for different plan types
 * - Compound interest formula: balance * (1 + rate)^(months/12)
 * - Proper handling of plan-specific rules (grace periods, time limits)
 * 
 * This legacy implementation is maintained to ensure existing integrations
 * continue to work without breaking changes.
 */
class TimeDepositCalculator {
    fun updateBalance(xs: List<TimeDeposit>) {
        for (i in xs.indices) {
            var interest = 0.0
            if (xs[i].days > 30) {
                if (xs[i].planType == "student") {
                    if (xs[i].days < 366) {
                        interest += xs[i].balance * 0.03 / 12
                    }
                } else if (xs[i].planType == "premium") {
                    if (xs[i].days > 45) {
                        interest += xs[i].balance * 0.05 / 12
                    }
                } else if (xs[i].planType == "basic") {
                    interest += xs[i].balance * 0.01 / 12
                }
            }
            val a2d = BigDecimal(interest).setScale(2, RoundingMode.HALF_UP)
            xs[i].balance += a2d.toDouble()
        }
    }
}