package tv.anilibria.module.data.network.entity.app

/**
 * Created by radiationx on 04.12.2017.
 */
data class PageResponse<T>(
    val items: List<T>,
    val pagination: PageMetaResponse
)
