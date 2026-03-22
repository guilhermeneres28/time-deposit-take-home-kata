package org.ikigaidigital.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.domain.calculator.BasicPlanCalculator
import org.ikigaidigital.domain.calculator.PremiumPlanCalculator
import org.ikigaidigital.domain.calculator.StudentPlanCalculator
import org.ikigaidigital.domain.error.DomainError
import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("TimeDepositService Tests")
class TimeDepositServiceTest {

    private lateinit var service: TimeDepositService

    @BeforeEach
    fun setup() {
        service = TimeDepositService(
            basicCalculator = BasicPlanCalculator(),
            studentCalculator = StudentPlanCalculator(),
            premiumCalculator = PremiumPlanCalculator()
        )
    }

    @Test
    fun `should calculate interest for basic plan deposit`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 61
        )

        val result = service.calculateInterest(deposit)

        assertThat(result.isSuccess()).isTrue()
        val interest = (result as DomainResult.Success).value
        assertThat(interest.toDouble()).isEqualTo(20.1)
    }

    @Test
    fun `should calculate interest for student plan deposit`() {
        val deposit = TimeDeposit(
            id = 2L,
            planType = PlanType.STUDENT,
            balance = Money.of(1000.0),
            days = 61
        )

        val result = service.calculateInterest(deposit)

        assertThat(result.isSuccess()).isTrue()
        val interest = (result as DomainResult.Success).value
        assertThat(interest.toDouble()).isEqualTo(60.9)
    }

    @Test
    fun `should calculate interest for premium plan deposit`() {
        val deposit = TimeDeposit(
            id = 3L,
            planType = PlanType.PREMIUM,
            balance = Money.of(1000.0),
            days = 76
        )

        val result = service.calculateInterest(deposit)

        assertThat(result.isSuccess()).isTrue()
        val interest = (result as DomainResult.Success).value
        assertThat(interest.toDouble()).isEqualTo(102.5)
    }

    @Test
    fun `should update balance with calculated interest for basic plan`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 31
        )

        val result = service.updateBalance(deposit)

        assertThat(result.isSuccess()).isTrue()
        val updatedDeposit = (result as DomainResult.Success).value
        assertThat(updatedDeposit.balance.toDouble()).isEqualTo(1010.0)
    }

    @Test
    fun `should update balance with calculated interest for student plan`() {
        val deposit = TimeDeposit(
            id = 2L,
            planType = PlanType.STUDENT,
            balance = Money.of(1000.0),
            days = 31
        )

        val result = service.updateBalance(deposit)

        assertThat(result.isSuccess()).isTrue()
        val updatedDeposit = (result as DomainResult.Success).value
        assertThat(updatedDeposit.balance.toDouble()).isEqualTo(1030.0)
    }

    @Test
    fun `should update balance with calculated interest for premium plan`() {
        val deposit = TimeDeposit(
            id = 3L,
            planType = PlanType.PREMIUM,
            balance = Money.of(1000.0),
            days = 46
        )

        val result = service.updateBalance(deposit)

        assertThat(result.isSuccess()).isTrue()
        val updatedDeposit = (result as DomainResult.Success).value
        assertThat(updatedDeposit.balance.toDouble()).isEqualTo(1050.0)
    }

    @Test
    fun `should not change balance when no interest is earned`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 15
        )

        val result = service.updateBalance(deposit)

        assertThat(result.isSuccess()).isTrue()
        val updatedDeposit = (result as DomainResult.Success).value
        assertThat(updatedDeposit.balance.toDouble()).isEqualTo(1000.0)
    }

    @Test
    fun `should handle student plan after year limit`() {
        val deposit = TimeDeposit(
            id = 2L,
            planType = PlanType.STUDENT,
            balance = Money.of(1000.0),
            days = 400
        )

        val result = service.updateBalance(deposit)

        assertThat(result.isSuccess()).isTrue()
        val updatedDeposit = (result as DomainResult.Success).value
        assertThat(updatedDeposit.balance.toDouble()).isEqualTo(1000.0)
    }
}
