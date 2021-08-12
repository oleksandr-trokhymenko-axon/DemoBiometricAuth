package com.axon.demobiometricauth.base.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Collect data from Coroutine Flow.
 * New coroutine will be launched in specified [fragment]'s view lifecycle coroutine scope for collecting data.
 * And in this case collecting data will be cancelled before calling Fragment.onDestroyView()
 *
 * @param T type of the flow data
 * @param fragment fragment view lifecycle coroutines scope will be using to launch new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T> Flow<T>.collect(
    fragment: Fragment,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return collect(fragment.viewLifecycleOwner, context, onlyOnce, action)
}

/**
 * Collect not nullable data from Coroutine Flow.
 * New coroutine will be launched in specified [fragment]'s view lifecycle coroutine scope for collecting data.
 * And in this case collecting data will be cancelled before calling Fragment.onDestroyView()
 *
 * @param T type of the flow data
 * @param fragment fragment view lifecycle coroutines scope will be using to launch new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T : Any> Flow<T?>.collectNotNull(
    fragment: Fragment,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return collectNotNull(fragment.viewLifecycleOwner, context, onlyOnce, action)
}

/**
 * Collect data from Coroutine Flow.
 * New coroutine will be launched in specified [owner]'s coroutine scope for collecting data
 *
 * @param T type of the flow data
 * @param owner lifecycle owner which coroutines scope will be using to launch new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T> Flow<T>.collect(
    owner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return collect(owner.lifecycleScope, context, onlyOnce, action)
}

/**
 * Collect not nullable data from Coroutine Flow.
 * New coroutine will be launched in specified [owner]'s coroutine scope for collecting data
 *
 * @param T type of the flow data
 * @param owner lifecycle owner which coroutines scope will be using to launch new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T : Any> Flow<T?>.collectNotNull(
    owner: LifecycleOwner,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return collectNotNull(owner.lifecycleScope, context, onlyOnce, action)
}

/**
 * Collect data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T> Flow<T>.collect(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return scope.launch(context) {
        collect {
            action.invoke(this, it)
            if (onlyOnce) {
                cancel()
            }
        }
    }
}

/**
 * Collect not nullable data from Coroutine Flow.
 * New coroutine will be launched in specified [scope] for collecting data
 *
 * @param T type of the flow data
 * @param scope coroutines scope where will be launched new coroutine for collecting flow data
 * @param context customizable coroutine scope context
 * @param onlyOnce is data should be collected only once. (Default: false)
 * @param action collect action that will called when new item will be produced
 * @return launched for collecting data coroutine's job
 */
inline fun <reified T : Any> Flow<T?>.collectNotNull(
    scope: CoroutineScope,
    context: CoroutineContext = EmptyCoroutineContext,
    onlyOnce: Boolean = false,
    crossinline action: suspend CoroutineScope.(value: T) -> Unit
): Job {
    return filterNotNull().collect(scope, context, onlyOnce, action)
}