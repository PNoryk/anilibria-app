package tv.anilibria.module.data.restapi.entity.app

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 04.12.2017.
 */
@JsonClass(generateAdapter = true)
data class PageResponse<T>(
    @Json(name = "items") val items: List<T>,
    @Json(name = "pagination") val meta: PageMetaResponse
)
