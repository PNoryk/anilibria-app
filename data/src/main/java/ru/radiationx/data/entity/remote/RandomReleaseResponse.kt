package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class RandomReleaseResponse(
    @SerializedName("code") val code: String
)