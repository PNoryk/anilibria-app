package ru.radiationx.data.api.converter

import ru.radiationx.data.adomain.*
import ru.radiationx.data.api.remote.*
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.data.system.ApiUtils
import ru.radiationx.shared.ktx.dateFromSec
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ReleaseConverter(
    private val dayConverter: DayConverter,
    private val apiConfig: ApiConfig,
    private val apiUtils: ApiUtils
) {

    fun toDomain(response: RandomReleaseResponse) = RandomRelease(
        code = response.code
    )

    fun toDomain(response: ReleaseResponse) = Release(
        id = response.id,
        code = response.code,
        names = response.names?.map { apiUtils.escapeHtml(it).toString() },
        series = response.series,
        poster = response.poster?.let { "${apiConfig.baseImagesUrl}$it" },
        favorite = response.favorite?.let { toDomain(it) },
        last = response.last?.let { parseLast(it) },
        webPlayer = response.moon,
        status = response.statusCode?.let { parseStatus(it) },
        type = response.type,
        genres = response.genres,
        voices = response.voices,
        year = response.year,
        day = response.day?.let { dayConverter.toDomain(it) },
        description = response.description?.trim(),
        announce = response.announce?.trim(),
        blockedInfo = response.blockedInfo?.let { toDomain(it) },
        playlist = response.playlist?.map { toDomain(it) },
        torrents = response.torrents?.map { toDomain(it) },
        showDonateDialog = response.showDonateDialog
    )

    fun toDomain(response: FavoriteInfoResponse) = FavoriteInfo(
        rating = response.rating,
        added = response.added
    )

    fun toDomain(response: BlockInfoResponse) = BlockInfo(
        blocked = response.blocked,
        reason = response.reason
    )

    fun toDomain(response: EpisodeResponse) = Episode(
        id = response.id,
        title = response.title,
        sd = response.sd,
        hd = response.hd,
        fullhd = response.fullhd,
        srcSd = response.srcSd,
        srcHd = response.srcHd
    )

    fun toDomain(response: TorrentResponse) = Torrent(
        id = response.id,
        hash = response.hash,
        leechers = response.leechers,
        seeders = response.seeders,
        completed = response.completed,
        quality = response.quality,
        series = response.series,
        size = response.size,
        time = response.time.dateFromSec(),
        url = response.url?.let { "${apiConfig.baseImagesUrl}$it" }
    )

    private fun parseStatus(status: String): Release.Status? = when (status) {
        "1" -> Release.Status.PROGRESS
        "2" -> Release.Status.COMPLETE
        "3" -> Release.Status.HIDDEN
        "4" -> Release.Status.ONGOING
        else -> null
    }

    private fun parseLast(last: String): Date? = try {
        last.toLong().dateFromSec()
    } catch (ex: Exception) {
        null
    }
}