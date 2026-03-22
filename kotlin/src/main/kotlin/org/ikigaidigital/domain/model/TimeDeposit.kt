package org.ikigaidigital.domain.model

data class TimeDeposit(
    val id: Long,
    val planType: PlanType,
    var balance: Money,
    val days: Int,
    val withdrawals: List<Withdrawal> = emptyList()
) {
    init {
        require(days >= 0) { "Days cannot be negative: $days" }
    }

    companion object {
        fun fromLegacy(legacy: org.ikigaidigital.TimeDeposit): TimeDeposit {
            val planType = PlanType.fromString(legacy.planType) 
                ?: throw IllegalArgumentException("Invalid plan type: ${legacy.planType}")
            
            return TimeDeposit(
                id = legacy.id.toLong(),
                planType = planType,
                balance = Money.of(legacy.balance),
                days = legacy.days
            )
        }
    }

    fun toLegacy(): org.ikigaidigital.TimeDeposit {
        return org.ikigaidigital.TimeDeposit(
            id = id.toInt(),
            planType = planType.name.lowercase(),
            balance = balance.toDouble(),
            days = days
        )
    }
}
