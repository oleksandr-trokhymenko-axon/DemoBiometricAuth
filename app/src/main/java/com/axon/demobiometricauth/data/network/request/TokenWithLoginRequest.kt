package com.axon.demobiometricauth.data.network.request

import com.google.gson.annotations.SerializedName

data class TokenWithLoginRequest(
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("request_token")
    val requestToken: String
)