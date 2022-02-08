package tv.anilibria.module.data.restapi.entity.app

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by radiationx on 04.12.2017.
 */
@JsonClass(generateAdapter = true)
data class PageMetaResponse(
    @Json(name = "page") val page: Int?,
    @Json(name = "perPage") val perPage: Int?,
    @Json(name = "allPages") val allPages: Int?,
    @Json(name = "allItems") val allItems: Int?,
) 
