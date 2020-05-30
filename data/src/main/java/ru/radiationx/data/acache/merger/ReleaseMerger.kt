package ru.radiationx.data.acache.merger

import ru.radiationx.data.acache.common.mergeField
import ru.radiationx.data.adomain.entity.release.Release
import toothpick.InjectConstructor

@InjectConstructor
class ReleaseMerger {

    fun merge(old: Release, new: Release) = Release(
        id = old.id.mergeField(new.id),
        code = old.code.mergeField(new.code),
        nameRu = old.nameRu.mergeField(new.nameRu),
        nameEn = old.nameEn.mergeField(new.nameEn),
        series = old.series.mergeField(new.series),
        poster = old.poster.mergeField(new.poster),
        favorite = old.favorite.mergeField(new.favorite),
        last = old.last.mergeField(new.last),
        webPlayer = old.webPlayer.mergeField(new.webPlayer),
        status = old.status.mergeField(new.status),
        type = old.type.mergeField(new.type),
        genres = old.genres.mergeField(new.genres),
        voices = old.voices.mergeField(new.voices),
        year = old.year.mergeField(new.year),
        day = old.day.mergeField(new.day),
        description = old.description.mergeField(new.description),
        announce = old.announce.mergeField(new.announce),
        blockedInfo = old.blockedInfo.mergeField(new.blockedInfo),
        playlist = old.playlist.mergeField(new.playlist),
        torrents = old.torrents.mergeField(new.torrents),
        showDonateDialog = old.showDonateDialog.mergeField(new.showDonateDialog)
    )

    fun filterSame(oldItems: List<Release>, newItems: List<Release>): List<Release> {
        val oldMap = oldItems.associateBy { it.id }
        val mergedItems = newItems.map { new ->
            oldMap[new.id]?.let { old -> merge(old, new) } ?: new
        }
        return mergedItems.minus(oldItems)
    }
}