package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

/**
 * Behavior tests for TimeDepositCalculator.
 * 
 * TODO: Current implementation applies interest ONLY ONCE, not cumulatively.
 *       These tests document the actual behavior before refactoring.
 *       After refactoring to apply interest monthly/cumulatively, these tests
 *       will need to be updated with correct expected values.
 */
@DisplayName("TimeDepositCalculator Tests")
class TimeDepositCalculatorTest {

    private val calculator = TimeDepositCalculator()
    private val tolerance = Offset.offset(0.01)

    @ParameterizedTest
    @MethodSource("provideBasicPlanScenarios")
    @DisplayName("Basic Plan: should calculate 1% monthly interest after 30 days")
    fun `updateBalance calculates correct interest for basic plan`(
        initialBalance: Double,
        days: Int,
        expectedBalance: Double
    ) {
        val deposits = listOf(TimeDeposit(1, "basic", initialBalance, days))
        
        calculator.updateBalance(deposits)
        
        assertThat(deposits[0].balance).isCloseTo(expectedBalance, tolerance)
    }

    @ParameterizedTest
    @MethodSource("provideStudentPlanScenarios")
    @DisplayName("Student Plan: should calculate 3% monthly interest after 30 days, no interest after 365 days")
    fun `updateBalance calculates correct interest for student plan`(
        initialBalance: Double,
        days: Int,
        expectedBalance: Double
    ) {
        val deposits = listOf(TimeDeposit(2, "student", initialBalance, days))
        
        calculator.updateBalance(deposits)
        
        assertThat(deposits[0].balance).isCloseTo(expectedBalance, tolerance)
    }

    @ParameterizedTest
    @MethodSource("providePremiumPlanScenarios")
    @DisplayName("Premium Plan: should calculate 5% monthly interest after 45 days")
    fun `updateBalance calculates correct interest for premium plan`(
        initialBalance: Double,
        days: Int,
        expectedBalance: Double
    ) {
        val deposits = listOf(TimeDeposit(3, "premium", initialBalance, days))
        
        calculator.updateBalance(deposits)
        
        assertThat(deposits[0].balance).isCloseTo(expectedBalance, tolerance)
    }

    @ParameterizedTest
    @MethodSource("provideMultipleDepositsScenarios")
    @DisplayName("Multiple Deposits: should calculate interest correctly for mixed plan types")
    fun `updateBalance handles multiple deposits with different plan types`(
        deposits: List<TimeDeposit>,
        expectedBalances: List<Double>
    ) {
        calculator.updateBalance(deposits)
        
        deposits.forEachIndexed { index, deposit ->
            assertThat(deposit.balance)
                .describedAs("Deposit ${deposit.id} (${deposit.planType}) balance")
                .isCloseTo(expectedBalances[index], tolerance)
        }
    }

    companion object {
        @JvmStatic
        fun provideBasicPlanScenarios() = listOf(
            Arguments.of(10000.0, 0, 10000.0),
            Arguments.of(10000.0, 15, 10000.0),
            Arguments.of(10000.0, 30, 10000.0),
            Arguments.of(10000.0, 31, 10008.33),
            Arguments.of(10000.0, 60, 10008.33),
            Arguments.of(10000.0, 90, 10008.33),
            Arguments.of(10000.0, 180, 10008.33),
            Arguments.of(10000.0, 365, 10008.33),
            Arguments.of(5000.0, 30, 5000.0),
            Arguments.of(1000.0, 60, 1000.83),
            Arguments.of(50000.0, 90, 50041.67),
            Arguments.of(1234567.0, 45, 1235595.81)
        )

        @JvmStatic
        fun provideStudentPlanScenarios() = listOf(
            Arguments.of(10000.0, 0, 10000.0),
            Arguments.of(10000.0, 15, 10000.0),
            Arguments.of(10000.0, 30, 10000.0),
            Arguments.of(10000.0, 31, 10025.0),
            Arguments.of(10000.0, 60, 10025.0),
            Arguments.of(10000.0, 90, 10025.0),
            Arguments.of(10000.0, 180, 10025.0),
            Arguments.of(10000.0, 365, 10025.0),
            Arguments.of(10000.0, 366, 10000.0),
            Arguments.of(10000.0, 400, 10000.0),
            Arguments.of(10000.0, 730, 10000.0),
            Arguments.of(5000.0, 365, 5012.5),
            Arguments.of(5000.0, 366, 5000.0),
            Arguments.of(1000.0, 60, 1002.5),
            Arguments.of(20000.0, 180, 20050.0)
        )

        @JvmStatic
        fun providePremiumPlanScenarios() = listOf(
            Arguments.of(10000.0, 0, 10000.0),
            Arguments.of(10000.0, 30, 10000.0),
            Arguments.of(10000.0, 45, 10000.0),
            Arguments.of(10000.0, 46, 10041.67),
            Arguments.of(10000.0, 75, 10041.67),
            Arguments.of(10000.0, 105, 10041.67),
            Arguments.of(10000.0, 180, 10041.67),
            Arguments.of(10000.0, 365, 10041.67),
            Arguments.of(5000.0, 45, 5000.0),
            Arguments.of(5000.0, 46, 5020.83),
            Arguments.of(1000.0, 75, 1004.17),
            Arguments.of(30000.0, 180, 30125.0)
        )

        @JvmStatic
        fun provideMultipleDepositsScenarios() = listOf(
            Arguments.of(
                listOf(
                    TimeDeposit(1, "basic", 10000.0, 60),
                    TimeDeposit(2, "student", 10000.0, 60),
                    TimeDeposit(3, "premium", 10000.0, 60)
                ),
                listOf(10008.33, 10025.0, 10041.67)
            ),
            Arguments.of(
                listOf(
                    TimeDeposit(1, "basic", 5000.0, 30),
                    TimeDeposit(2, "student", 5000.0, 30),
                    TimeDeposit(3, "premium", 5000.0, 45)
                ),
                listOf(5000.0, 5000.0, 5000.0)
            ),
            Arguments.of(
                listOf(
                    TimeDeposit(1, "basic", 10000.0, 365),
                    TimeDeposit(2, "student", 10000.0, 366),
                    TimeDeposit(3, "premium", 10000.0, 365)
                ),
                listOf(10008.33, 10000.0, 10041.67)
            ),
            Arguments.of(
                listOf(
                    TimeDeposit(1, "basic", 1000.0, 31),
                    TimeDeposit(2, "student", 2000.0, 365),
                    TimeDeposit(3, "premium", 3000.0, 46)
                ),
                listOf(1000.83, 2005.0, 3012.5)
            )
        )
    }
}