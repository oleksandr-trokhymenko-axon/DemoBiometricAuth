package com.axon.demobiometricauth.base.extensions

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.GsonBuilder
import kotlin.reflect.KClass

private val gson = GsonBuilder()
    .create()

inline fun <reified T : Any?> SharedPreferences.get(key: String): T? {
    return check(key, null) { get<T?>(key, null, T::class) }
}

inline fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T): T =
    get(key, defaultValue, T::class)

fun SharedPreferences.put(key: String, value: Any?, commit: Boolean = false) {
    edit(commit = commit) {
        when (value) {
            null -> remove(key)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            else -> putString(key, gson.toJson(value))
        }
    }
}


@PublishedApi
internal fun <T> SharedPreferences.get(key: String, defaultValue: T, clazz: KClass<*>): T {
    return when (clazz) {
        Boolean::class -> check(key, defaultValue) { getBoolean(key, false) }
        Float::class -> check(key, defaultValue) { getFloat(key, -1F) }
        Int::class -> check(key, defaultValue) { getInt(key, -1) }
        Long::class -> check(key, defaultValue) { getLong(key, -1) }
        String::class -> check(key, defaultValue) { getString(key, null) }
        else -> get<String?>(key)?.let { gson.fromJson<T>(it, clazz.java) } ?: defaultValue
    }
}

@Suppress("unchecked_cast")
@PublishedApi
internal fun <T> SharedPreferences.check(key: String, defaultValue: T, action: () -> Any?): T {
    return if (contains(key)) action() as T else defaultValue
}