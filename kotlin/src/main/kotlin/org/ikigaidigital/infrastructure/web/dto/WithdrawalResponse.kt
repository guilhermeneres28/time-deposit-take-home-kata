package org.ikigaidigital.infrastructure.web.dto

import java.math.BigDecimal
import java.time.LocalDate

data class WithdrawalResponse(
    val id: Long,
    val amount: BigDecimal,
    val date: LocalDate
)
