package org.ikigaidigital.domain.error

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DomainError Tests")
class DomainErrorTest {

    @Test
    fun `ValidationError should contain message`() {
        val error = DomainError.ValidationError("Invalid value")
        
        assertThat(error.message).isEqualTo("Invalid value")
    }

    @Test
    fun `NotFoundError should contain message`() {
        val error = DomainError.NotFoundError("Entity not found")
        
        assertThat(error.message).isEqualTo("Entity not found")
    }

    @Test
    fun `BusinessRuleViolation should contain message`() {
        val error = DomainError.BusinessRuleViolation("Rule violated")
        
        assertThat(error.message).isEqualTo("Rule violated")
    }

    @Test
    fun `InfrastructureError should contain message and optional cause`() {
        val cause = RuntimeException("DB error")
        val error = DomainError.InfrastructureError("Database connection failed", cause)
        
        assertThat(error.message).isEqualTo("Database connection failed")
        assertThat(error.cause).isEqualTo(cause)
    }

    @Test
    fun `InfrastructureError should work without cause`() {
        val error = DomainError.InfrastructureError("Service unavailable")
        
        assertThat(error.message).isEqualTo("Service unavailable")
        assertThat(error.cause).isNull()
    }
}
