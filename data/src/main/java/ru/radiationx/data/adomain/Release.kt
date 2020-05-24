package ru.radiationx.data.adomain

import java.util.*


data class Release(
    val id: Int,
    val code: String?,
    val names: List<String>?,
    val series: String?,
    val poster: String?,
    val favorite: FavoriteInfo?,
    val last: Date?,
    val webPlayer: String?,
    val status: Status?,
    val type: String?,
    val genres: List<String>?,
    val voices: List<String>?,
    val year: String?,
    val day: Int?,
    val description: String?,
    val blockedInfo: BlockInfo?,
    val playlist: List<Episode>?,
    val torrents: List<Torrent>?
) {

    enum class Status {
        PROGRESS,
        COMPLETE,
        HIDDEN,
        ONGOING
    }
}