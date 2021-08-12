package com.axon.demobiometricauth.base.delegator

import android.content.SharedPreferences
import com.axon.demobiometricauth.base.extensions.get
import com.axon.demobiometricauth.base.extensions.put
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

@PublishedApi
internal class PreferencesDelegator<T : Any>(
    private val prefs: SharedPreferences,
    private val key: String,
    private val clazz: KClass<*>,
    private val defValue: T
) : ReadWriteProperty<Any, T> {

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        prefs.put(key, value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return prefs.get(key, defValue, clazz)
    }
}

inline fun <reified T : Any> SharedPreferences.value(
    key: String,
    defValue: T
): ReadWriteProperty<Any, T> {
    return PreferencesDelegator(this, key, T::class, defValue)
}
