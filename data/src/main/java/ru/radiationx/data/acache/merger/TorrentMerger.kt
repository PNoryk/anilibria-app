package ru.radiationx.data.acache.merger

import ru.radiationx.data.acache.common.mergeField
import ru.radiationx.data.adomain.entity.release.Episode
import ru.radiationx.data.adomain.entity.release.Torrent
import toothpick.InjectConstructor

@InjectConstructor
class TorrentMerger {

    fun merge(old: Torrent, new: Torrent) = Torrent(
        releaseId = old.releaseId.mergeField(new.releaseId),
        id = old.id.mergeField(new.id),
        hash = old.hash.mergeField(new.hash),
        leechers = old.leechers.mergeField(new.leechers),
        seeders = old.seeders.mergeField(new.seeders),
        completed = old.completed.mergeField(new.completed),
        quality = old.quality.mergeField(new.quality),
        series = old.series.mergeField(new.series),
        size = old.size.mergeField(new.size),
        time = old.time.mergeField(new.time),
        url = old.url.mergeField(new.url)
    )

    fun filterSame(oldItems: List<Torrent>, newItems: List<Torrent>): List<Torrent> {
        val oldGrouped = oldItems.groupBy { it.releaseId }
        val newGrouped = newItems.groupBy { it.releaseId }

        val filteredGroups = newGrouped
            .map { entry ->
                oldGrouped[entry.key]?.let { filterGroup(it, entry.value) } ?: entry.value
            }
            .flatten()
        return filteredGroups.minus(oldItems)
    }

    private fun filterGroup(oldItems: List<Torrent>, newItems: List<Torrent>): List<Torrent> {
        val oldMap = oldItems.associateBy { it.id }
        val mergedItems = newItems.map { newItem ->
            oldMap[newItem.id]?.let { merge(it, newItem) } ?: newItem
        }
        return mergedItems.minus(oldItems)
    }
}