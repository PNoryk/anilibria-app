package tv.anilibria.feature.appupdates.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 28.01.18.
 */
@JsonClass(generateAdapter = true)
data class UpdateDataWrapperResponse(
    @Json(name = "update") val update: UpdateDataResponse,
)