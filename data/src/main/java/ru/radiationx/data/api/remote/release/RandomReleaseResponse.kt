package ru.radiationx.data.api.remote.release


import com.google.gson.annotations.SerializedName

data class RandomReleaseResponse(
    @SerializedName("code") val code: String
)