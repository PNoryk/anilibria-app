package tv.anilibria.feature.content.data.local

import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.local.holders.ReleaseCacheLocalDataSource
import tv.anilibria.feature.content.types.release.Release

@InjectConstructor
class ReleaseCacheHelper(
    private val dataSource: ReleaseCacheLocalDataSource
) {

    suspend fun update(data: List<Release>, strategy: CacheUpdateStrategy) {
        when (strategy) {
            CacheUpdateStrategy.REPLACE -> dataSource.put(data)
            CacheUpdateStrategy.MERGE -> {
                val currentData = dataSource.get()
                val newData = data.map { newRelease ->
                    currentData[newRelease.id]
                        ?.let { oldRelease ->
                            update(oldRelease, newRelease, strategy)
                        }
                        ?: newRelease
                }
                dataSource.put(newData)
            }
        }
    }
}

enum class CacheUpdateStrategy {
    REPLACE,
    MERGE
}

private fun update(
    old: Release,
    new: Release,
    strategy: CacheUpdateStrategy
): Release = when (strategy) {
    CacheUpdateStrategy.REPLACE -> new
    CacheUpdateStrategy.MERGE -> Release(
        id = updateField(old.id, new.id),
        code = updateField(old.code, new.code),
        names = updateField(old.names, new.names),
        series = updateField(old.series, new.series),
        poster = updateField(old.poster, new.poster),
        torrentUpdate = updateField(old.torrentUpdate, new.torrentUpdate),
        statusName = updateField(old.statusName, new.statusName),
        status = updateField(old.status, new.status),
        type = updateField(old.type, new.type),
        genres = updateField(old.genres, new.genres),
        voices = updateField(old.voices, new.voices),
        year = updateField(old.year, new.year),
        season = updateField(old.season, new.season),
        scheduleDay = updateField(old.scheduleDay, new.scheduleDay),
        description = updateField(old.description, new.description),
        announce = updateField(old.announce, new.announce),
        favoriteInfo = updateField(old.favoriteInfo, new.favoriteInfo),
        showDonateDialog = updateField(old.showDonateDialog, new.showDonateDialog),
        blockedInfo = updateField(old.blockedInfo, new.blockedInfo),
        webPlayerUrl = updateField(old.webPlayerUrl, new.webPlayerUrl),
        episodes = updateField(old.episodes, new.episodes),
        externalPlaylists = updateField(old.externalPlaylists, new.externalPlaylists),
        torrents = updateField(old.torrents, new.torrents),
    )
}

private fun <T> updateField(old: T, new: T): T {
    return if (old == null && new != null && old != new) {
        new
    } else {
        old
    }
}