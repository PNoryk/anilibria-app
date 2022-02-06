package tv.anilibria.module.data.network.entity.app.release

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 25.01.18.
 */
@JsonClass(generateAdapter = true)
data class FavoriteInfoResponse(
    @Json(name = "rating") val rating: Int,
    @Json(name = "added") val isAdded: Boolean
)