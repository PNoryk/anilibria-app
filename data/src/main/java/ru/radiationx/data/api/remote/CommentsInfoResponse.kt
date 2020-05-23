package ru.radiationx.data.api.remote


import com.google.gson.annotations.SerializedName

data class CommentsInfoResponse(
    @SerializedName("baseUrl") val baseUrl: String,
    @SerializedName("script") val script: String
)