package org.ikigaidigital.infrastructure.persistence.repository

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.infrastructure.persistence.entity.TimeDepositEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

@DisplayName("TimeDepositEntity Conversion Tests")
class TimeDepositRepositoryIntegrationTest {

    @Test
    fun `should convert entity to domain model`() {
        val entity = TimeDepositEntity(
            id = 1L,
            planType = "BASIC",
            balance = BigDecimal("1000.0000"),
            days = 31
        )

        val domain = entity.toDomain()

        assertThat(domain.id).isEqualTo(1L)
        assertThat(domain.planType.name).isEqualTo("BASIC")
        assertThat(domain.balance.toDouble()).isEqualTo(1000.0)
        assertThat(domain.days).isEqualTo(31)
    }

    @Test
    fun `should convert domain model to entity`() {
        val domain = org.ikigaidigital.domain.model.TimeDeposit(
            id = 1L,
            planType = org.ikigaidigital.domain.model.PlanType.BASIC,
            balance = org.ikigaidigital.domain.model.Money.of(1000.0),
            days = 31
        )

        val entity = TimeDepositEntity.fromDomain(domain)

        assertThat(entity.id).isEqualTo(1L)
        assertThat(entity.planType).isEqualTo("BASIC")
        assertThat(entity.balance).isEqualByComparingTo(BigDecimal("1000.0000"))
        assertThat(entity.days).isEqualTo(31)
    }
}
