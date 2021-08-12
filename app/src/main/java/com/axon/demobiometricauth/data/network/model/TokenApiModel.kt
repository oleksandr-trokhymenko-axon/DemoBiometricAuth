package com.axon.demobiometricauth.data.network.model

import com.google.gson.annotations.SerializedName

data class TokenApiModel(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String
)