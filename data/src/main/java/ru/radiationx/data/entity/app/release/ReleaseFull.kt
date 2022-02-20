package ru.radiationx.data.entity.app.release

import java.io.Serializable

@Deprecated("old data")
class ReleaseFull() : ReleaseItem(), Serializable {

    constructor(item: ReleaseItem) : this() {
        id = item.id
        code = item.code
        link = item.link
        names.addAll(item.names)
        series = item.series
        poster = item.poster
        torrentUpdate = item.torrentUpdate
        status = item.status
        statusCode = item.statusCode
        announce = item.announce
        types.addAll(item.types)
        genres.addAll(item.genres)
        voices.addAll(item.voices)
        seasons.addAll(item.seasons)
        days.addAll(item.days)
        description = item.description

        isNew = item.isNew
    }

    var showDonateDialog: Boolean = false


    var moonwalkLink: String? = null
    val episodes = mutableListOf<Episode>()

    class Episode : Serializable {
        var releaseId = 0
        var id: Int = 0
        var seek: Long = 0
        var isViewed: Boolean = false
        var lastAccess: Long = 0

        var title: String? = null
        var urlSd: String? = null
        var urlHd: String? = null
        var urlFullHd: String? = null

    }


}
