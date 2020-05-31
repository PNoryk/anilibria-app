package ru.radiationx.data.acache.merger

import ru.radiationx.data.acache.common.mergeField
import anilibria.tv.domain.entity.release.Episode
import toothpick.InjectConstructor

@InjectConstructor
class EpisodeMerger {

    fun merge(old: Episode, new: Episode) =
        Episode(
            releaseId = old.releaseId.mergeField(new.releaseId),
            id = old.id.mergeField(new.id),
            title = old.title.mergeField(new.title),
            sd = old.sd.mergeField(new.sd),
            hd = old.hd.mergeField(new.hd),
            fullhd = old.fullhd.mergeField(new.fullhd),
            srcSd = old.srcSd.mergeField(new.srcSd),
            srcHd = old.srcHd.mergeField(new.srcHd)
        )

    fun filterSame(oldItems: List<Episode>, newItems: List<Episode>): List<Episode> {
        val oldGrouped = oldItems.groupBy { it.releaseId }
        val newGrouped = newItems.groupBy { it.releaseId }

        val filteredGroups = newGrouped
            .map { entry ->
                oldGrouped[entry.key]?.let { filterGroup(it, entry.value) } ?: entry.value
            }
            .flatten()
        return filteredGroups.minus(oldItems)
    }

    private fun filterGroup(oldItems: List<Episode>, newItems: List<Episode>): List<Episode> {
        val oldMap = oldItems.associateBy { it.id }
        val mergedItems = newItems.map { newItem ->
            oldMap[newItem.id]?.let { merge(it, newItem) } ?: newItem
        }
        return mergedItems.minus(oldItems)
    }
}