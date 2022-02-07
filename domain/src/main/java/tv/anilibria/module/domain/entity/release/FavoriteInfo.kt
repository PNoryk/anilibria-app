package tv.anilibria.module.domain.entity.release

import tv.anilibria.module.domain.entity.common.Count

/**
 * Created by radiationx on 25.01.18.
 */
data class FavoriteInfo(
    val rating: Count,
    val isAdded: Boolean
)