package org.ikigaidigital.infrastructure.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "withdrawals")
class WithdrawalEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_deposit_id", nullable = false)
    val timeDeposit: TimeDepositEntity,
    
    @Column(nullable = false, precision = 19, scale = 4)
    val amount: BigDecimal,
    
    @Column(nullable = false)
    val date: LocalDate
)
