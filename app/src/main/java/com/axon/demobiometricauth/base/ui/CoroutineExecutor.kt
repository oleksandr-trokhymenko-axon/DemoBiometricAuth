package com.axon.demobiometricauth.base.ui

import com.axon.demobiometricauth.base.utils.GlobalExceptionLogger
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class CoroutineExecutor(
    dispatcher: CoroutineDispatcher = Dispatchers.Main
) {

    private val exceptionHandler: CoroutineContext = CoroutineExceptionHandler { _, throwable ->
        GlobalExceptionLogger.logException(throwable)
        handleException(throwable)
    }

    private val scope = CoroutineScope(dispatcher + SupervisorJob() + exceptionHandler)

    private val _error by lazy { MutableSharedFlow<AppError>() }
    val error by lazy { _error.asSharedFlow() }

    private val _progress by lazy { MutableStateFlow(false) }
    val progress by lazy { _progress.asStateFlow() }

    fun cancelScope() {
        scope.coroutineContext.cancelChildren()
    }

    fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        withProgress: Boolean = false,
        exceptionHandler: ((Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return scope.launch(context) {
            execute(withProgress, exceptionHandler, block)
        }
    }

    private suspend fun execute(
        withProgress: Boolean,
        exceptionHandler: ((Exception) -> Boolean)?,
        block: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            if (withProgress) {
                _progress.value = true
            }
            try {
                block.invoke(this)
            } catch (e: Exception) {
                GlobalExceptionLogger.logException(e)
                if (exceptionHandler?.invoke(e) != false) {
                    handleException(e)
                }
            } finally {
                if (withProgress) {
                    _progress.value = false
                }
            }
        }
    }

    private fun handleException(throwable: Throwable) {
        val appError = when (throwable) {
            is SocketTimeoutException -> TimeoutError(throwable)
            is UnknownHostException -> NoNetworkError(throwable)
            else -> OtherError(throwable)
        }
        _error.tryEmit(appError)
    }
}