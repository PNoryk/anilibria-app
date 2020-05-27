package ru.radiationx.data.api.entity.schedule


import com.google.gson.annotations.SerializedName
import ru.radiationx.data.api.entity.release.ReleaseResponse

data class ScheduleDayResponse(
    @SerializedName("day") val day: String,
    @SerializedName("items") val items: List<ReleaseResponse>
)