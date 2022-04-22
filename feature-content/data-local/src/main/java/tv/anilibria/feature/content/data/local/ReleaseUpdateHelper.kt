package tv.anilibria.feature.content.data.local

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.holders.ReleaseUpdatesLocalDataSource
import tv.anilibria.feature.content.types.ReleaseUpdate
import tv.anilibria.feature.content.types.release.Release

@InjectConstructor
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