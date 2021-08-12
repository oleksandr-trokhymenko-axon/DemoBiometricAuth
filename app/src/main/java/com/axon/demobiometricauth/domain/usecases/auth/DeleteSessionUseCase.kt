package com.axon.demobiometricauth.domain.usecases.auth

import com.axon.demobiometricauth.base.usecase.UseCaseWithoutParams
import com.axon.demobiometricauth.data.local.EncryptPrefsManager
import com.axon.demobiometricauth.data.network.request.DeleteSessionRequest
import com.axon.demobiometricauth.data.repositories.UserRepository
import javax.inject.Inject

class DeleteSessionUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val encryptPrefsManager: EncryptPrefsManager,
    private val prefsManager: EncryptPrefsManager
) : UseCaseWithoutParams<Unit>() {
    override suspend fun execute() {
        val request = DeleteSessionRequest(encryptPrefsManager.sessionId)
        userRepository.deleteSession(request)
        prefsManager.sessionId = ""
    }
}