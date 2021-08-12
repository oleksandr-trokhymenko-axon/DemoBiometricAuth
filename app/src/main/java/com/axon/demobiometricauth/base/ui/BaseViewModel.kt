package com.axon.demobiometricauth.base.ui

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(dispatcher: CoroutineDispatcher = Dispatchers.Default) : ViewModel() {

    private val coroutineExecutor = CoroutineExecutor(dispatcher)

    val progress: StateFlow<Boolean>
        get() = coroutineExecutor.progress

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        coroutineExecutor.cancelScope()
    }

    protected fun runCoroutine(
        context: CoroutineContext = EmptyCoroutineContext,
        withProgress: Boolean = false,
        exceptionHandler: ((Exception) -> Boolean)? = null,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return coroutineExecutor.runCoroutine(context, withProgress, exceptionHandler, block)
    }
}