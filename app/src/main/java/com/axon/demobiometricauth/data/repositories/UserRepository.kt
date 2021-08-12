package com.axon.demobiometricauth.data.repositories

import com.axon.demobiometricauth.data.local.EncryptPrefsManager
import com.axon.demobiometricauth.data.network.services.UserService
import com.axon.demobiometricauth.data.network.model.TokenApiModel
import com.axon.demobiometricauth.data.network.request.DeleteSessionRequest
import com.axon.demobiometricauth.data.network.request.SessionRequest
import com.axon.demobiometricauth.data.network.request.TokenWithLoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
    private val encryptPrefsManager: EncryptPrefsManager
) {

    suspend fun getRequestToken(): TokenApiModel {
        return userService.getRequestToken()
    }

    suspend fun getSession(request: SessionRequest): Boolean {
        val session = userService.getSession(request)
        if (session.success) {
            encryptPrefsManager.sessionId = session.sessionId
        }
        return session.success
    }

    suspend fun getSessionWithLogin(request: TokenWithLoginRequest): TokenApiModel {
        return userService.getSessionWithLogin(request)
    }

    suspend fun deleteSession(request: DeleteSessionRequest) {
        userService.deleteSession(request)
    }
}