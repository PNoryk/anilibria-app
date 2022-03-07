package tv.anilibria.feature.domain.entity

/**
 * Created by radiationx on 04.12.2017.
 */
data class Page<T>(
    val items: List<T>,
    val meta: PageMeta
)
