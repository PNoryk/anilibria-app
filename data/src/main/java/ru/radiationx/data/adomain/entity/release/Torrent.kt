package ru.radiationx.data.adomain.entity.release

import java.util.*


data class Torrent(
    val releaseId: Int,
    val id: Int,
    val hash: String?,
    val leechers: Int,
    val seeders: Int,
    val completed: Int,
    val quality: String?,
    val series: String?,
    val size: Long,
    val time: Date,
    val url: String?
)