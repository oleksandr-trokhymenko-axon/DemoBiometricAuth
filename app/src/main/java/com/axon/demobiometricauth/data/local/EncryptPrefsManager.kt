package com.axon.demobiometricauth.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.axon.demobiometricauth.base.delegator.value
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncryptPrefsManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences = run {
        createEncryptedSharedPreferences(context)
            ?: context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    }

    var sessionId: String by prefs.value(PREF_SESSION_ID, "")

    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences? {
        return try {
            EncryptedSharedPreferences.create(
                PREFS_FILENAME,
                MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e("error", "createEncryptedSharedPreferences: $e")
            null
        }
    }

    companion object {
        private const val PREFS_FILENAME = "prefs_encrypt_scout"
        private const val PREF_SESSION_ID = "session_id"
    }
}