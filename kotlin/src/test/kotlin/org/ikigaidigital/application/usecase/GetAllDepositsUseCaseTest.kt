package org.ikigaidigital.application.usecase

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.ikigaidigital.domain.port.TimeDepositPort
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@DisplayName("GetAllDepositsUseCase Tests")
class GetAllDepositsUseCaseTest {

    private lateinit var timeDepositPort: TimeDepositPort
    private lateinit var useCase: GetAllDepositsUseCase

    @BeforeEach
    fun setup() {
        timeDepositPort = mock()
        useCase = GetAllDepositsUseCase(timeDepositPort)
    }

    @Test
    fun `should return all deposits`() {
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

        val result = useCase.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1L)
        assertThat(result[0].planType).isEqualTo(PlanType.BASIC)
        assertThat(result[1].id).isEqualTo(2L)
        assertThat(result[1].planType).isEqualTo(PlanType.STUDENT)
        verify(timeDepositPort).findAll()
    }

    @Test
    fun `should return empty list when no deposits exist`() {
        whenever(timeDepositPort.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertThat(result).isEmpty()
        verify(timeDepositPort).findAll()
    }

    @Test
    fun `should correctly return domain models`() {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.PREMIUM,
            balance = Money.of(5000.0),
            days = 76
        )

        whenever(timeDepositPort.findAll()).thenReturn(listOf(deposit))

        val result = useCase.execute()

        assertThat(result).hasSize(1)
        val returnedDeposit = result[0]
        assertThat(returnedDeposit.id).isEqualTo(1L)
        assertThat(returnedDeposit.planType).isEqualTo(PlanType.PREMIUM)
        assertThat(returnedDeposit.balance.toDouble()).isEqualTo(5000.0)
        assertThat(returnedDeposit.days).isEqualTo(76)
    }
}
