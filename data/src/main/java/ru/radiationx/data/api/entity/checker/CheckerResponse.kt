package ru.radiationx.data.api.entity.checker


import com.google.gson.annotations.SerializedName

data class CheckerResponse(
    @SerializedName("update") val update: UpdateResponse
)