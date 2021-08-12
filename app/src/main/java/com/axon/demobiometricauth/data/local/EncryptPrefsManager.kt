package com.axon.demobiometricauth.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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

    var masterKey = MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private fun createEncryptedSharedPreferences(context: Context): SharedPreferences? {
        return try {
            EncryptedSharedPreferences.create(
                context,
                PREFS_FILENAME,
                masterKey,
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