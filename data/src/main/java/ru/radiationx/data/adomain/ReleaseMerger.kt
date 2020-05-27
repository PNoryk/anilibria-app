package ru.radiationx.data.adomain

import ru.radiationx.data.adomain.entity.release.Release

object ReleaseMerger {

    fun merge(old: Release, new: Release): Release = Release(
        old.id.merge(new.id),
        old.code.merge(new.code),
        old.nameRu.merge(new.nameRu),
        old.nameEn.merge(new.nameEn),
        old.series.merge(new.series),
        old.poster.merge(new.poster),
        old.favorite.merge(new.favorite),
        old.last.merge(new.last),
        old.webPlayer.merge(new.webPlayer),
        old.status.merge(new.status),
        old.type.merge(new.type),
        old.genres.merge(new.genres),
        old.voices.merge(new.voices),
        old.year.merge(new.year),
        old.day.merge(new.day),
        old.description.merge(new.description),
        old.announce.merge(new.announce),
        old.blockedInfo.merge(new.blockedInfo),
        old.playlist.merge(new.playlist),
        old.torrents.merge(new.torrents),
        old.showDonateDialog.merge(new.showDonateDialog)
    )

    private fun <T> T.merge(new: T): T {
        return if (this != new || (this == null && new != null)) new else this
    }

}