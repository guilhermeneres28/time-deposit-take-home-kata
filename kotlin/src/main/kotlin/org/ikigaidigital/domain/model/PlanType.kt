package org.ikigaidigital.domain.model

enum class PlanType {
    BASIC,
    STUDENT,
    PREMIUM;

    companion object {
        fun fromString(value: String): PlanType = 
            entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Invalid plan type: $value")
    }
}
