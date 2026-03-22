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

@DisplayName("StudentPlanCalculator Tests")
class StudentPlanCalculatorTest {

    private val calculator = StudentPlanCalculator()

    @ParameterizedTest
    @MethodSource("provideStudentPlanScenarios")
    fun `should calculate correct interest for student plan`(
        initialBalance: Double,
        days: Int,
        expectedInterest: Double
    ) {
        val deposit = TimeDeposit(
            id = 1L,
            planType = PlanType.STUDENT,
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
        fun provideStudentPlanScenarios() = listOf(
            Arguments.of(1000.0, 0, 0.0),
            Arguments.of(1000.0, 1, 0.0),
            Arguments.of(1000.0, 15, 0.0),
            Arguments.of(1000.0, 30, 0.0),
            Arguments.of(5000.0, 30, 0.0),
            Arguments.of(1000.0, 31, 30.0),
            Arguments.of(5000.0, 31, 150.0),
            Arguments.of(1000.0, 61, 60.9),
            Arguments.of(5000.0, 61, 304.5),
            Arguments.of(1000.0, 92, 92.727),
            Arguments.of(5000.0, 92, 463.635),
            Arguments.of(1000.0, 183, 194.0523),
            Arguments.of(5000.0, 183, 970.2615),
            Arguments.of(1000.0, 365, 425.7609),
            Arguments.of(5000.0, 365, 2128.8044),
            Arguments.of(1000.0, 366, 0.0),
            Arguments.of(5000.0, 366, 0.0),
            Arguments.of(1000.0, 400, 0.0),
            Arguments.of(5000.0, 400, 0.0),
            Arguments.of(100.0, 31, 3.0),
            Arguments.of(10000.0, 31, 300.0),
            Arguments.of(1000.0, 32, 30.0),
            Arguments.of(1000.0, 60, 30.0),
            Arguments.of(1000.0, 90, 60.9),
            Arguments.of(1000.0, 91, 92.727),
        )
    }
}
