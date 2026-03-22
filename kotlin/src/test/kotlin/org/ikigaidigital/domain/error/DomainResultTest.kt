package org.ikigaidigital.domain.error

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("DomainResult Tests")
class DomainResultTest {

    @Test
    fun `success should create Success result with value`() {
        val result = DomainResult.success("test value")
        
        assertThat(result).isInstanceOf(DomainResult.Success::class.java)
        assertThat(result.isSuccess()).isTrue()
        assertThat(result.isFailure()).isFalse()
        assertThat((result as DomainResult.Success).value).isEqualTo("test value")
    }

    @Test
    fun `failure should create Failure result with error`() {
        val error = DomainError.ValidationError("Invalid input")
        val result = DomainResult.failure<String>(error)
        
        assertThat(result).isInstanceOf(DomainResult.Failure::class.java)
        assertThat(result.isSuccess()).isFalse()
        assertThat(result.isFailure()).isTrue()
        assertThat((result as DomainResult.Failure).error).isEqualTo(error)
    }

    @Test
    fun `map should transform success value`() {
        val result = DomainResult.success(5)
        val mapped = result.map { it * 2 }
        
        assertThat(mapped.isSuccess()).isTrue()
        assertThat((mapped as DomainResult.Success).value).isEqualTo(10)
    }

    @Test
    fun `map should preserve failure`() {
        val error = DomainError.ValidationError("Error")
        val result = DomainResult.failure<Int>(error)
        val mapped = result.map { it * 2 }
        
        assertThat(mapped.isFailure()).isTrue()
        assertThat((mapped as DomainResult.Failure).error).isEqualTo(error)
    }

    @Test
    fun `flatMap should chain success operations`() {
        val result = DomainResult.success(5)
        val chained = result.flatMap { DomainResult.success(it * 2) }
        
        assertThat(chained.isSuccess()).isTrue()
        assertThat((chained as DomainResult.Success).value).isEqualTo(10)
    }

    @Test
    fun `flatMap should short-circuit on failure`() {
        val error = DomainError.ValidationError("Error")
        val result = DomainResult.failure<Int>(error)
        val chained = result.flatMap { DomainResult.success(it * 2) }
        
        assertThat(chained.isFailure()).isTrue()
        assertThat((chained as DomainResult.Failure).error).isEqualTo(error)
    }

    @Test
    fun `getOrNull should return value on success`() {
        val result = DomainResult.success("value")
        
        assertThat(result.getOrNull()).isEqualTo("value")
    }

    @Test
    fun `getOrNull should return null on failure`() {
        val result = DomainResult.failure<String>(DomainError.ValidationError("Error"))
        
        assertThat(result.getOrNull()).isNull()
    }

    @Test
    fun `getOrElse should return value on success`() {
        val result = DomainResult.success("value")
        
        assertThat(result.getOrElse { "default" }).isEqualTo("value")
    }

    @Test
    fun `getOrElse should return default on failure`() {
        val result = DomainResult.failure<String>(DomainError.ValidationError("Error"))
        
        assertThat(result.getOrElse { "default" }).isEqualTo("default")
    }

    @Test
    fun `fold should handle success case`() {
        val result = DomainResult.success(5)
        val folded = result.fold(
            onSuccess = { it * 2 },
            onFailure = { 0 }
        )
        
        assertThat(folded).isEqualTo(10)
    }

    @Test
    fun `fold should handle failure case`() {
        val result = DomainResult.failure<Int>(DomainError.ValidationError("Error"))
        val folded = result.fold(
            onSuccess = { it * 2 },
            onFailure = { 0 }
        )
        
        assertThat(folded).isEqualTo(0)
    }
}
