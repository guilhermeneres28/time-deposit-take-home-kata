package org.ikigaidigital.domain.calculator

import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.TimeDeposit
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

abstract class BaseInterestCalculator : InterestCalculator {
    
    protected abstract val monthlyInterestRate: Double
    protected abstract val daysThreshold: Int
    protected abstract val daysPerMonth: Int
    protected abstract val scale: Int
    protected abstract val roundingMode: RoundingMode
    
    override fun calculate(deposit: TimeDeposit): DomainResult<Money> {
        val months = calculateMonths(deposit.days)
        
        if (months == 0) {
            return DomainResult.success(Money.ZERO)
        }
        
        val interest = calculateCompoundInterest(deposit.balance.amount, months)
        return DomainResult.success(Money.of(interest.setScale(scale, roundingMode)))
    }
    
    protected open fun calculateMonths(days: Int): Int {
        return if (days <= daysThreshold) 0 
               else ((days - daysThreshold - 1) / daysPerMonth) + 1
    }
    
    private fun calculateCompoundInterest(principal: BigDecimal, months: Int): BigDecimal {
        val compoundFactor = (1.0 + monthlyInterestRate).pow(months)
        val finalAmount = principal.multiply(BigDecimal.valueOf(compoundFactor))
        return finalAmount.subtract(principal)
    }
}
