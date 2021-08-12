package com.axon.demobiometricauth.base.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCaseWithoutParams<out T>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {

    protected abstract suspend fun execute(): T

    suspend operator fun invoke(): T = withContext(dispatcher) {
        execute()
    }
}