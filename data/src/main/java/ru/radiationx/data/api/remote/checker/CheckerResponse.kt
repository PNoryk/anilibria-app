package ru.radiationx.data.api.remote.checker


import com.google.gson.annotations.SerializedName

data class CheckerResponse(
    @SerializedName("update") val update: UpdateResponse
)