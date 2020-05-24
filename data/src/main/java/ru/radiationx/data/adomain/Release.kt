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
    val status: String?,
    val type: String?,
    val genres: List<String>?,
    val voices: List<String>?,
    val year: Int?,
    val day: Int?,
    val description: String?,
    val blockedInfo: BlockInfo?,
    val playlist: List<Episode>?,
    val torrents: List<Torrent>?
)