package org.ikigaidigital.domain.model

import java.time.LocalDate

data class Withdrawal(
    val id: Long,
    val amount: Money,
    val date: LocalDate,
    val depositId: Long
) {
    init {
        require(amount > Money.ZERO) { "Withdrawal amount must be positive: $amount" }
    }
}
