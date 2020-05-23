package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class BlockInfoResponse(
    @SerializedName("blocked") val blocked: Boolean,
    @SerializedName("reason") val reason: String?
)