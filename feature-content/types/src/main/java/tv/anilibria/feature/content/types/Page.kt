package tv.anilibria.feature.content.types

/**
 * Created by radiationx on 04.12.2017.
 */
data class Page<T>(
    val items: List<T>,
    val meta: PageMeta
)
