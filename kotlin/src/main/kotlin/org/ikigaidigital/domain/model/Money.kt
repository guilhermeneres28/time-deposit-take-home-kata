package org.ikigaidigital.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money(val amount: BigDecimal) : Comparable<Money> {
    
    init {
        require(amount >= BigDecimal.ZERO) { "Money amount cannot be negative: $amount" }
    }

    operator fun plus(other: Money): Money = Money(amount + other.amount)
    
    operator fun minus(other: Money): Money = Money(amount - other.amount)
    
    operator fun times(multiplier: Double): Money = 
        Money(amount.multiply(BigDecimal.valueOf(multiplier)))
    
    operator fun times(multiplier: BigDecimal): Money = 
        Money(amount.multiply(multiplier))
    
    override fun compareTo(other: Money): Int = amount.compareTo(other.amount)
    
    fun toDouble(): Double = amount.toDouble()
    
    fun scale(scale: Int = 2, roundingMode: RoundingMode = RoundingMode.HALF_UP): Money =
        Money(amount.setScale(scale, roundingMode))

    companion object {
        val ZERO = Money(BigDecimal.ZERO)
        
        fun of(value: Double): Money = Money(BigDecimal.valueOf(value))
        
        fun of(value: BigDecimal): Money = Money(value)
        
        fun of(value: String): Money = Money(BigDecimal(value))
    }
}
