package ru.radiationx.data.api.remote.schedule


import com.google.gson.annotations.SerializedName
import ru.radiationx.data.api.remote.release.ReleaseResponse

data class ScheduleDayResponse(
    @SerializedName("day") val day: String,
    @SerializedName("items") val items: List<ReleaseResponse>
)