package ru.radiationx.data.adomain


import com.google.gson.annotations.SerializedName

data class BlockInfoResponse(
    @SerializedName("blocked") val blocked: Boolean,
    @SerializedName("reason") val reason: String?
)