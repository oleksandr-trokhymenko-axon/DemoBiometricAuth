package com.axon.demobiometricauth.domain.usecases.auth

import com.axon.demobiometricauth.base.usecase.UseCaseWithoutParamsSync
import com.axon.demobiometricauth.data.local.EncryptPrefsManager
import javax.inject.Inject

class IsUserLoggedUseCase @Inject constructor(private val prefsManager: EncryptPrefsManager) :
    UseCaseWithoutParamsSync<Boolean> {
    override fun invoke(): Boolean {
        return prefsManager.sessionId.isNotEmpty()
    }
}