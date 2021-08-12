package com.axon.demobiometricauth.data.network.request

import com.google.gson.annotations.SerializedName

data class DeleteSessionRequest(
    @SerializedName("session_id")
    val sessionId: String
)
