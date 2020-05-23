package ru.radiationx.data.api.remote.auth

import com.google.gson.annotations.SerializedName

data class SocialServiceResponse(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("socialUrl") val socialUrl: String,
    @SerializedName("resultPattern") val resultPattern: String,
    @SerializedName("errorUrlPattern") val errorUrlPattern: String
)