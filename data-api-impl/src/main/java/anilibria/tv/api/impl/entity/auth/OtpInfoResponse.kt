package anilibria.tv.api.impl.entity.auth

import com.google.gson.annotations.SerializedName

data class OtpInfoResponse(
    @SerializedName("code") val code: String,
    @SerializedName("description") val description: String,
    @SerializedName("expiresAt") val expiresAt: Long,
    @SerializedName("remainingTime") val remainingTime: Long
)