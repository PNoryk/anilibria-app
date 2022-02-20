package ru.radiationx.data.entity.app.release

import java.io.Serializable

/* Created by radiationx on 31.10.17. */

@Deprecated("old data")
open class ReleaseItem : Serializable {
    var id: Int = -1
    var code: String? = null
    val names = mutableListOf<String>()
    var series: String? = null
    var poster: String? = null
    var torrentUpdate: Int = 0
    var status: String? = null
    var statusCode: String? = null
    val types = mutableListOf<String>()
    val genres = mutableListOf<String>()
    val voices = mutableListOf<String>()
    val seasons = mutableListOf<String>()
    val days = mutableListOf<String>()
    var description: String? = null
    var announce: String? = null

    var isNew: Boolean = false

    var link: String? = null

    val title: String?
        get() = names.firstOrNull()

    val titleEng: String?
        get() = names.lastOrNull()


    companion object {
        const val STATUS_CODE_PROGRESS = "1"
        const val STATUS_CODE_COMPLETE = "2"
        const val STATUS_CODE_HIDDEN = "3"
        const val STATUS_CODE_NOT_ONGOING = "4"
    }
}
