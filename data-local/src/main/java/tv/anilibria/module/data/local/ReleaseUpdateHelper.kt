package tv.anilibria.module.data.local

import tv.anilibria.module.data.local.holders.ReleaseUpdatesLocalDataSource
import tv.anilibria.module.domain.entity.ReleaseUpdate
import tv.anilibria.module.domain.entity.release.Release

class ReleaseUpdateHelper(
    private val dataSource: ReleaseUpdatesLocalDataSource
) {

    suspend fun update(data: List<Release>) {
        val updates = data.mapNotNull { release ->
            release.torrentUpdate?.let { torrentUpdate ->
                ReleaseUpdate(release.id, torrentUpdate)
            }
        }
        dataSource.put(updates)
    }
}