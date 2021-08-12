package com.axon.demobiometricauth.domain.usecases.auth

import com.axon.demobiometricauth.base.usecase.UseCase
import com.axon.demobiometricauth.data.network.request.SessionRequest
import com.axon.demobiometricauth.data.network.request.TokenWithLoginRequest
import com.axon.demobiometricauth.data.repositories.UserRepository
import javax.inject.Inject

class GetSessionWithLoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) : UseCase<GetSessionWithLoginUseCase.Params, Boolean>() {

    override suspend fun execute(params: Params): Boolean {
        val token = userRepository.getRequestToken().requestToken
        val tokenWithLoginRequest = TokenWithLoginRequest(params.username, params.password, token)
        val approvedToken =
            userRepository.getSessionWithLogin(request = tokenWithLoginRequest).requestToken
        return userRepository.getSession(request = SessionRequest(approvedToken))
    }

    data class Params(
        val username: String,
        val password: String
    )
}