package com.axon.demobiometricauth.base.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<in P, out T>(private val dispatcher: CoroutineDispatcher = Dispatchers.Default) {

    protected abstract suspend fun execute(params: P): T

    suspend operator fun invoke(params: P): T = withContext(dispatcher) {
        execute(params)
    }
}