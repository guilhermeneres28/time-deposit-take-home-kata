package org.ikigaidigital.infrastructure.integration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.offset
import org.ikigaidigital.application.usecase.GetAllDepositsUseCase
import org.ikigaidigital.application.usecase.UpdateAllBalancesUseCase
import org.ikigaidigital.domain.model.PlanType
import org.ikigaidigital.infrastructure.persistence.repository.TimeDepositRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@DisplayName("Time Deposit Integration Tests with Testcontainers")
class TimeDepositIntegrationTest {

    companion object {
        @Container
        @JvmStatic
        val postgresContainer = PostgreSQLContainer("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
            registry.add("spring.datasource.username", postgresContainer::getUsername)
            registry.add("spring.datasource.password", postgresContainer::getPassword)
            registry.add("spring.flyway.enabled") { "true" }
        }
    }

    @Autowired
    private lateinit var updateAllBalancesUseCase: UpdateAllBalancesUseCase

    @Autowired
    private lateinit var getAllDepositsUseCase: GetAllDepositsUseCase

    @Autowired
    private lateinit var repository: TimeDepositRepository

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun resetData() {
        jdbcTemplate.update("UPDATE time_deposits SET balance = 1000.0 WHERE plan_type = 'BASIC' AND days = 31")
        jdbcTemplate.update("UPDATE time_deposits SET balance = 10000.0 WHERE plan_type = 'BASIC' AND days = 15")
        jdbcTemplate.update("UPDATE time_deposits SET balance = 2000.0 WHERE plan_type = 'STUDENT' AND days = 61")
        jdbcTemplate.update("UPDATE time_deposits SET balance = 3000.0 WHERE plan_type = 'STUDENT' AND days = 365")
        jdbcTemplate.update("UPDATE time_deposits SET balance = 5000.0 WHERE plan_type = 'PREMIUM' AND days = 76")
        jdbcTemplate.update("UPDATE time_deposits SET balance = 7500.0 WHERE plan_type = 'PREMIUM' AND days = 46")
    }

    @Test
    fun `should verify Flyway migrations created tables and loaded sample data`() {
        val deposits = getAllDepositsUseCase.execute()
        assertThat(deposits).hasSize(6)
        val basicDeposits = deposits.filter { it.planType == PlanType.BASIC }
        assertThat(basicDeposits).hasSize(2)
        assertThat(basicDeposits.map { it.balance.toDouble() }).containsExactlyInAnyOrder(1000.0, 10000.0)
        
        val studentDeposits = deposits.filter { it.planType == PlanType.STUDENT }
        assertThat(studentDeposits).hasSize(2)
        assertThat(studentDeposits.map { it.balance.toDouble() }).containsExactlyInAnyOrder(2000.0, 3000.0)
        
        val premiumDeposits = deposits.filter { it.planType == PlanType.PREMIUM }
        assertThat(premiumDeposits).hasSize(2)
        assertThat(premiumDeposits.map { it.balance.toDouble() }).containsExactlyInAnyOrder(5000.0, 7500.0)
    }

    @Test
    fun `should update all balances with correct interest calculations and persist to database`() {
        val initialDeposits = getAllDepositsUseCase.execute()
        assertThat(initialDeposits).hasSize(6)

        updateAllBalancesUseCase.execute()

        val updatedDeposits = getAllDepositsUseCase.execute()
        assertThat(updatedDeposits).hasSize(6)

        val basic31Days = updatedDeposits.first { it.planType == PlanType.BASIC && it.days == 31 }
        assertThat(basic31Days.balance.toDouble()).isCloseTo(1010.0, offset(0.01))

        val basic15Days = updatedDeposits.first { it.planType == PlanType.BASIC && it.days == 15 }
        assertThat(basic15Days.balance.toDouble()).isCloseTo(10000.0, offset(0.01))

        val student61Days = updatedDeposits.first { it.planType == PlanType.STUDENT && it.days == 61 }
        assertThat(student61Days.balance.toDouble()).isCloseTo(2121.8, offset(0.1))

        val student365Days = updatedDeposits.first { it.planType == PlanType.STUDENT && it.days == 365 }
        assertThat(student365Days.balance.toDouble()).isCloseTo(4277.28, offset(0.1))

        val premium76Days = updatedDeposits.first { it.planType == PlanType.PREMIUM && it.days == 76 }
        assertThat(premium76Days.balance.toDouble()).isCloseTo(5512.5, offset(0.1))

        val premium46Days = updatedDeposits.first { it.planType == PlanType.PREMIUM && it.days == 46 }
        assertThat(premium46Days.balance.toDouble()).isCloseTo(7875.0, offset(0.1))
    }

    @Test
    fun `should persist updated balances to PostgreSQL database`() {
        val initialCount = repository.count()
        assertThat(initialCount).isEqualTo(6)

        updateAllBalancesUseCase.execute()

        val finalCount = repository.count()
        assertThat(finalCount).isEqualTo(6)

        val allEntities = repository.findAll()
        assertThat(allEntities).allMatch { entity ->
            when {
                entity.planType == "BASIC" && entity.days == 31 -> entity.balance.toDouble() > 1000.0
                entity.planType == "STUDENT" && entity.days == 61 -> entity.balance.toDouble() > 2000.0
                entity.planType == "PREMIUM" && entity.days == 76 -> entity.balance.toDouble() > 5000.0
                else -> true
            }
        }
    }

    @Test
    fun `should handle multiple update cycles correctly`() {
        val initial = getAllDepositsUseCase.execute()
        val initialBasic31 = initial.first { it.planType == PlanType.BASIC && it.days == 31 }
        assertThat(initialBasic31.balance.toDouble()).isEqualTo(1000.0)

        updateAllBalancesUseCase.execute()
        val afterFirst = getAllDepositsUseCase.execute()
        val firstBasic31 = afterFirst.first { it.planType == PlanType.BASIC && it.days == 31 }
        assertThat(firstBasic31.balance.toDouble()).isCloseTo(1010.0, offset(0.01))

        updateAllBalancesUseCase.execute()
        val afterSecond = getAllDepositsUseCase.execute()
        val secondBasic31 = afterSecond.first { it.planType == PlanType.BASIC && it.days == 31 }
        assertThat(secondBasic31.balance.toDouble()).isGreaterThan(firstBasic31.balance.toDouble())
        assertThat(secondBasic31.balance.toDouble()).isCloseTo(1020.1, offset(0.1))
    }

    @Test
    fun `should verify PostgreSQL container is running and accessible`() {
        assertThat(postgresContainer.isRunning).isTrue()
        assertThat(postgresContainer.databaseName).isEqualTo("testdb")
        assertThat(postgresContainer.username).isEqualTo("test")
        
        val deposits = getAllDepositsUseCase.execute()
        assertThat(deposits).isNotEmpty
    }
}
