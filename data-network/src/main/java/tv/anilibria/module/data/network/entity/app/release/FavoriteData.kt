package tv.anilibria.module.data.network.entity.app.release

import tv.anilibria.module.data.network.entity.app.PaginatedResponse

/**
 * Created by radiationx on 26.01.18.
 */
class FavoriteData {
    lateinit var sessId: String
    lateinit var items: PaginatedResponse<List<ReleaseItem>>
}