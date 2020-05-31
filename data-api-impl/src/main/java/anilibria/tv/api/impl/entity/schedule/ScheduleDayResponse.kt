package anilibria.tv.api.impl.entity.schedule


import com.google.gson.annotations.SerializedName
import anilibria.tv.api.impl.entity.release.ReleaseResponse

data class ScheduleDayResponse(
    @SerializedName("day") val day: String,
    @SerializedName("items") val items: List<ReleaseResponse>
)