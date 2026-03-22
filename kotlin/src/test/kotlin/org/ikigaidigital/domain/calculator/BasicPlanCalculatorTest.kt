package org.ikigaidigital.domain.calculator

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@DisplayName("BasicPlanCalculator Tests")
class BasicPlanCalculatorTest {

    private val calculator = BasicPlanCalculator()

    @ParameterizedTest
    @MethodSource("provideBasicPlanScenarios")
    fun `should calculate correct interest for basic plan`(
        initialBalance: Double,
        days: Int,
        expectedInterest: Double
    ) {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.BASIC,
            balance = Money.of(initialBalance),
            days = days
        )

        val result = calculator.calculate(deposit)

        assertThat(result.isSuccess()).isTrue()
        val interest = (result as DomainResult.Success).value
        assertThat(interest.toDouble()).isCloseTo(expectedInterest, org.assertj.core.data.Offset.offset(0.0001))
    }

    companion object {
        @JvmStatic
        fun provideBasicPlanScenarios() = listOf(
            Arguments.of(1000.0, 0, 0.0),
            Arguments.of(1000.0, 1, 0.0),
            Arguments.of(1000.0, 15, 0.0),
            Arguments.of(1000.0, 30, 0.0),
            Arguments.of(5000.0, 30, 0.0),
            Arguments.of(1000.0, 31, 10.0),
            Arguments.of(5000.0, 31, 50.0),
            Arguments.of(1000.0, 61, 20.1),
            Arguments.of(5000.0, 61, 100.5),
            Arguments.of(1000.0, 92, 30.301),
            Arguments.of(5000.0, 92, 151.505),
            Arguments.of(1000.0, 183, 61.5202),
            Arguments.of(5000.0, 183, 307.6008),
            Arguments.of(1000.0, 365, 126.8250),
            Arguments.of(5000.0, 365, 634.1252),
            Arguments.of(100.0, 31, 1.0),
            Arguments.of(10000.0, 31, 100.0),
            Arguments.of(1000.0, 32, 10.0),
            Arguments.of(1000.0, 60, 10.0),
            Arguments.of(1000.0, 90, 20.1),
            Arguments.of(1000.0, 91, 30.301),
        )
    }
}
