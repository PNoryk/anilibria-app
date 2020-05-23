package ru.radiationx.data.entity.remote


import com.google.gson.annotations.SerializedName

data class ScheduleDayResponse(
    @SerializedName("day") val day: String,
    @SerializedName("items") val items: List<ReleaseResponse>
)