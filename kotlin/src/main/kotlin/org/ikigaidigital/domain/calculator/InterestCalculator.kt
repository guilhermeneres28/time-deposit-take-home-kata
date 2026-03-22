package org.ikigaidigital.domain.calculator

import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.TimeDeposit

interface InterestCalculator {
    fun calculate(deposit: TimeDeposit): DomainResult<Money>
}
