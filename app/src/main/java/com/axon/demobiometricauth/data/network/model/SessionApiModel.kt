package com.axon.demobiometricauth.data.network.model

import com.google.gson.annotations.SerializedName

data class SessionApiModel(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)