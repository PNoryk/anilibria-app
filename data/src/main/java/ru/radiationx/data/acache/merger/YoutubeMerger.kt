package ru.radiationx.data.acache.merger

import ru.radiationx.data.acache.common.mergeField
import anilibria.tv.domain.entity.release.Release
import anilibria.tv.domain.entity.youtube.Youtube
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class YoutubeMerger {

    fun merge(old: Youtube, new: Youtube) =
        Youtube(
            id = old.id.mergeField(new.id),
            title = old.title.mergeField(new.title),
            image = old.image.mergeField(new.image),
            vid = old.vid.mergeField(new.vid),
            views = old.views.mergeField(new.views),
            comments = old.comments.mergeField(new.comments),
            timestamp = old.timestamp.mergeField(new.timestamp)
        )

    fun filterSame(oldItems: List<Youtube>, newItems: List<Youtube>): List<Youtube> {
        val oldMap = oldItems.associateBy { it.id }
        val mergedItems = newItems.map { new ->
            oldMap[new.id]?.let { old -> merge(old, new) } ?: new
        }
        return mergedItems.minus(oldItems)
    }
}