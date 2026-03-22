package org.ikigaidigital.domain.error

sealed class DomainResult<out T> {
    data class Success<T>(val value: T) : DomainResult<T>()
    data class Failure<T>(val error: DomainError) : DomainResult<T>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun getOrElse(default: () -> @UnsafeVariance T): T = when (this) {
        is Success -> value
        is Failure -> default()
    }

    inline fun <R> map(transform: (T) -> R): DomainResult<R> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> Failure(error)
    }

    inline fun <R> flatMap(transform: (T) -> DomainResult<R>): DomainResult<R> = when (this) {
        is Success -> transform(value)
        is Failure -> Failure(error)
    }

    inline fun <R> fold(
        onSuccess: (T) -> R,
        onFailure: (DomainError) -> R
    ): R = when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure(error)
    }

    companion object {
        fun <T> success(value: T): DomainResult<T> = Success(value)
        fun <T> failure(error: DomainError): DomainResult<T> = Failure(error)
    }
}
