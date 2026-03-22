package org.ikigaidigital.domain.service

import org.ikigaidigital.domain.calculator.BasicPlanCalculator
import org.ikigaidigital.domain.calculator.InterestCalculator
import org.ikigaidigital.domain.calculator.PremiumPlanCalculator
import org.ikigaidigital.domain.calculator.StudentPlanCalculator
import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit

class TimeDepositService(
    private val basicCalculator: BasicPlanCalculator,
    private val studentCalculator: StudentPlanCalculator,
    private val premiumCalculator: PremiumPlanCalculator
) {
    
    fun calculateInterest(deposit: TimeDeposit): DomainResult<Money> {
        val calculator = getCalculatorForPlan(deposit.planType)
        return calculator.calculate(deposit)
    }
    
    fun updateBalance(deposit: TimeDeposit): DomainResult<TimeDeposit> {
        return calculateInterest(deposit).map { interest ->
            deposit.copy(balance = deposit.balance + interest)
        }
    }
    
    private fun getCalculatorForPlan(planType: PlanType): InterestCalculator {
        return when (planType) {
            PlanType.BASIC -> basicCalculator
            PlanType.STUDENT -> studentCalculator
            PlanType.PREMIUM -> premiumCalculator
        }
    }
}
