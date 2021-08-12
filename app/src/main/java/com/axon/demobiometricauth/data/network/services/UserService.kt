package com.axon.demobiometricauth.data.network.services

import com.axon.demobiometricauth.data.network.model.SessionApiModel
import com.axon.demobiometricauth.data.network.model.TokenApiModel
import com.axon.demobiometricauth.data.network.request.DeleteSessionRequest
import com.axon.demobiometricauth.data.network.request.SessionRequest
import com.axon.demobiometricauth.data.network.request.TokenWithLoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface UserService {
    @GET("authentication/token/new")
    suspend fun getRequestToken(): TokenApiModel

    @POST("authentication/session/new")
    suspend fun getSession(@Body request: SessionRequest): SessionApiModel

    @POST("authentication/token/validate_with_login")
    suspend fun getSessionWithLogin(@Body request: TokenWithLoginRequest): TokenApiModel

    @HTTP(method = "DELETE", path = "authentication/session", hasBody = true)
    suspend fun deleteSession(@Body request: DeleteSessionRequest): Response<Unit>
}