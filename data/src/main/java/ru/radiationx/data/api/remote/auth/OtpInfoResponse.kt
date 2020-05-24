package ru.radiationx.data.api.remote.auth

import com.google.gson.annotations.SerializedName
import java.util.*

data class OtpInfoResponse(
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String,
    @SerializedName("expiresAt") val expiresAt: Long,
    @SerializedName("remainingTime") val remainingTime: Long
)