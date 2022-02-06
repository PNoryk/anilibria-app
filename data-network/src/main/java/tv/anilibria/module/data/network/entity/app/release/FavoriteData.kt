package tv.anilibria.module.data.network.entity.app.release

import tv.anilibria.module.data.network.entity.app.Paginated

/**
 * Created by radiationx on 26.01.18.
 */
class FavoriteData {
    lateinit var sessId: String
    lateinit var items: Paginated<List<ReleaseItem>>
}