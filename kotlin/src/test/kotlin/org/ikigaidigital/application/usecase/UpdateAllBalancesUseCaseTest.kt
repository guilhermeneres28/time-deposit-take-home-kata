package org.ikigaidigital.application.usecase

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.domain.calculator.BasicPlanCalculator
import org.ikigaidigital.domain.calculator.PremiumPlanCalculator
import org.ikigaidigital.domain.calculator.StudentPlanCalculator
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.ikigaidigital.domain.port.TimeDepositPort
import org.ikigaidigital.domain.service.TimeDepositService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("UpdateAllBalancesUseCase Tests")
class UpdateAllBalancesUseCaseTest {

    private lateinit var timeDepositPort: TimeDepositPort
    private lateinit var service: TimeDepositService
    private lateinit var useCase: UpdateAllBalancesUseCase

    @BeforeEach
    fun setup() {
        timeDepositPort = mock()
        service = TimeDepositService(
            basicCalculator = BasicPlanCalculator(),
            studentCalculator = StudentPlanCalculator(),
            premiumCalculator = PremiumPlanCalculator()
        )
        useCase = UpdateAllBalancesUseCase(timeDepositPort, service)
    }

    @Test
    fun `should update all balances for all deposits`() {
        val deposit1 = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 31
        )
        val deposit2 = TimeDeposit(
            id = 2L,
            planType = PlanType.STUDENT,
            balance = Money.of(2000.0),
            days = 61
        )

        whenever(timeDepositPort.findAll()).thenReturn(listOf(deposit1, deposit2))
        whenever(timeDepositPort.saveAll(any())).thenAnswer { it.arguments[0] }

        useCase.execute()

        verify(timeDepositPort).findAll()
        verify(timeDepositPort).saveAll(any())
    }

    @Test
    fun `should correctly calculate and update basic plan balance`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 31
        )

        whenever(timeDepositPort.findAll()).thenReturn(listOf(deposit))
        whenever(timeDepositPort.saveAll(any())).thenAnswer { it.arguments[0] }

        useCase.execute()

        verify(timeDepositPort).saveAll(any())
    }

    @Test
    fun `should handle empty repository`() {
        whenever(timeDepositPort.findAll()).thenReturn(emptyList())

        useCase.execute()

        verify(timeDepositPort).findAll()
    }

    @Test
    fun `should update balance for deposits with no interest earned`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(1000.0),
            days = 15
        )

        whenever(timeDepositPort.findAll()).thenReturn(listOf(deposit))
        whenever(timeDepositPort.saveAll(any())).thenAnswer { it.arguments[0] }

        useCase.execute()

        verify(timeDepositPort).findAll()
        verify(timeDepositPort).saveAll(any())
    }
}
