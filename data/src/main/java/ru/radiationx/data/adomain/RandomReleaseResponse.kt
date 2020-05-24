package ru.radiationx.data.adomain


import com.google.gson.annotations.SerializedName

data class RandomReleaseResponse(
    @SerializedName("code") val code: String
)