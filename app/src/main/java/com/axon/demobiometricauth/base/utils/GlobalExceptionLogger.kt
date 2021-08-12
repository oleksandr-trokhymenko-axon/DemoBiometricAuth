package com.axon.demobiometricauth.base.utils

object GlobalExceptionLogger {
    private var logger: ((Throwable) -> Unit)? = null

    fun logException(throwable: Throwable) {
        logger?.invoke(throwable)
    }
}