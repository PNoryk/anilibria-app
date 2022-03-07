package tv.anilibria.feature.content.data.local

import tv.anilibria.feature.content.data.local.holders.ReleaseUpdatesLocalDataSource
import tv.anilibria.feature.content.types.ReleaseUpdate
import tv.anilibria.feature.content.types.release.Release

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