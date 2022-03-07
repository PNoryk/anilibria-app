/*
package tv.anilibria.module.data.merger

import Release

interface EntityMerger<T> {

    fun merge(old: T?, new: T, replace: Boolean): T
}

object ReleaseMerger : EntityMerger<Release> {
    override fun merge(old: Release?, new: Release, replace: Boolean): Release {
        if (old == null || replace) {
            return new
        }
        return Release(
            id = old.id.merge(new.id),
            code = old.code.merge(new.code),
            names = old.names.merge(new.names),
            series = old.series.merge(new.series),
            poster = old.poster.merge(new.poster),
            torrentUpdate = old.torrentUpdate.merge(new.torrentUpdate),
            statusName = old.statusName.merge(new.statusName),
            status = old.status.merge(new.status),
            type = old.type.merge(new.type),
            genres = old.genres.merge(new.genres),
            voices = old.voices.merge(new.voices),
            year = old.year.merge(new.year),
            season = old.season.merge(new.season),
            scheduleDay = old.scheduleDay.merge(new.scheduleDay),
            description = old.description.merge(new.description),
            announce = old.announce.merge(new.announce),
            favoriteInfo = old.favoriteInfo.merge(new.favoriteInfo),
            showDonateDialog = old.showDonateDialog.merge(new.showDonateDialog),
            blockedInfo = old.blockedInfo.merge(new.blockedInfo),
            webPlayerUrl = old.webPlayerUrl.merge(new.webPlayerUrl),
            episodes = old.episodes.merge(new.episodes),
            externalPlaylists = old.externalPlaylists.merge(new.externalPlaylists),
            torrents = old.torrents.merge(new.torrents),
        )
    }
}


fun <T> T.merge(new: T): T {
    return if (new != null && this != new) {
        new
    } else {
        this
    }
}*/
