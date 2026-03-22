package org.ikigaidigital.domain.error

sealed class DomainError(open val message: String) {
    data class ValidationError(override val message: String) : DomainError(message)
    data class NotFoundError(override val message: String) : DomainError(message)
    data class BusinessRuleViolation(override val message: String) : DomainError(message)
    data class InfrastructureError(
        override val message: String,
        val cause: Throwable? = null
    ) : DomainError(message)
}
