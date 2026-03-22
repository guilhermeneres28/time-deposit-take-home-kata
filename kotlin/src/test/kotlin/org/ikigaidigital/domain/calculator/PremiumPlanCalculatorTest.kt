package org.ikigaidigital.domain.calculator

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.ikigaidigital.domain.error.DomainResult
import org.ikigaidigital.domain.model.Money
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.domain.model.TimeDeposit
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@DisplayName("PremiumPlanCalculator Tests")
class PremiumPlanCalculatorTest {

    private val calculator = PremiumPlanCalculator()

    @ParameterizedTest
    @MethodSource("providePremiumPlanScenarios")
    fun `should calculate correct interest for premium plan`(
        initialBalance: Double,
        days: Int,
        expectedInterest: Double
    ) {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.PREMIUM,
            balance = Money.of(initialBalance),
            days = days
        )

        val result = calculator.calculate(deposit)

        assertThat(result.isSuccess()).isTrue()
        val interest = (result as DomainResult.Success).value
        assertThat(interest.toDouble()).isCloseTo(expectedInterest, offset(0.0001))
    }

    companion object {
        @JvmStatic
        fun providePremiumPlanScenarios() = listOf(
            Arguments.of(1000.0, 0, 0.0),
            Arguments.of(1000.0, 1, 0.0),
            Arguments.of(1000.0, 15, 0.0),
            Arguments.of(1000.0, 30, 0.0),
            Arguments.of(1000.0, 45, 0.0),
            Arguments.of(5000.0, 45, 0.0),
            Arguments.of(1000.0, 46, 50.0),
            Arguments.of(5000.0, 46, 250.0),
            Arguments.of(1000.0, 76, 102.5),
            Arguments.of(5000.0, 76, 512.5),
            Arguments.of(1000.0, 107, 157.625),
            Arguments.of(5000.0, 107, 788.125),
            Arguments.of(1000.0, 198, 340.0956),
            Arguments.of(5000.0, 198, 1700.4782),
            Arguments.of(1000.0, 380, 795.8563),
            Arguments.of(5000.0, 380, 3979.2817),
            Arguments.of(100.0, 46, 5.0),
            Arguments.of(10000.0, 46, 500.0),
            Arguments.of(1000.0, 47, 50.0),
            Arguments.of(1000.0, 75, 50.0),
            Arguments.of(1000.0, 105, 102.5),
            Arguments.of(1000.0, 106, 157.625),
        )
    }
}
