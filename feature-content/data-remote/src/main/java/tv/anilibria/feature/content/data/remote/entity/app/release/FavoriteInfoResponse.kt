package tv.anilibria.feature.content.data.remote.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 25.01.18.
 */
@JsonClass(generateAdapter = true)
data class FavoriteInfoResponse(
    @Json(name = "rating") val rating: Long,
    @Json(name = "added") val isAdded: Boolean
)