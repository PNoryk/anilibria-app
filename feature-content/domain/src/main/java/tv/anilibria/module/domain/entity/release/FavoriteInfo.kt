package tv.anilibria.module.domain.entity.release

import tv.anilibria.core.types.Count

/**
 * Created by radiationx on 25.01.18.
 */
data class FavoriteInfo(
    val rating: Count,
    val isAdded: Boolean
)