package org.ikigaidigital.infrastructure.persistence.entity

import jakarta.persistence.*
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import java.math.BigDecimal

@Entity
@Table(name = "time_deposits")
class TimeDepositEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @Column(nullable = false)
    val planType: String,
    
    @Column(nullable = false, precision = 19, scale = 4)
    val balance: BigDecimal,
    
    @Column(nullable = false)
    val days: Int,
    
    @OneToMany(mappedBy = "timeDeposit", cascade = [CascadeType.ALL], orphanRemoval = true)
    val withdrawals: MutableList<WithdrawalEntity> = mutableListOf()
) {
    fun toDomain(): TimeDeposit {
        return TimeDeposit(
            id = id ?: 0L,
            planType = PlanType.fromString(planType),
            balance = Money.of(balance),
            days = days
        )
    }
    
    companion object {
        fun fromDomain(domain: TimeDeposit): TimeDepositEntity {
            return TimeDepositEntity(
                id = if (domain.id == 0L) null else domain.id,
                planType = domain.planType.name,
                balance = domain.balance.amount,
                days = domain.days
            )
        }
    }
}
