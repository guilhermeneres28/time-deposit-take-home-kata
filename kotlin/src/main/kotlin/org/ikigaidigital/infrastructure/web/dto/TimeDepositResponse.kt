package org.ikigaidigital.infrastructure.web.dto

import org.ikigaidigital.domain.model.TimeDeposit
import java.math.BigDecimal

data class TimeDepositResponse(
    val id: Long,
    val planType: String,
    val balance: BigDecimal,
    val days: Int,
    val withdrawals: List<WithdrawalResponse> = emptyList()
) {
    companion object {
        fun fromDomain(domain: TimeDeposit): TimeDepositResponse {
            return TimeDepositResponse(
                id = domain.id,
                planType = domain.planType.name,
                balance = domain.balance.amount,
                days = domain.days
            )
        }
    }
}
