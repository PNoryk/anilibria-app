package ru.radiationx.data.api.converter

import anilibria.tv.domain.entity.release.*
import ru.radiationx.data.adomain.entity.release.*
import ru.radiationx.data.api.entity.release.*
import ru.radiationx.data.system.ApiUtils
import ru.radiationx.shared.ktx.dateFromSec
import toothpick.InjectConstructor
import java.util.*

@InjectConstructor
class ReleaseConverter(
    private val dayConverter: DayConverter,
    private val apiUtils: ApiUtils
) {

    fun toDomain(response: RandomReleaseResponse) = RandomRelease(
        code = response.code
    )

    fun toDomain(response: ReleaseResponse) = Release(
        id = response.id,
        code = response.code,
        nameRu = response.names?.getOrNull(0)?.let { apiUtils.escapeHtml(it).toString() },
        nameEn = response.names?.getOrNull(1)?.let { apiUtils.escapeHtml(it).toString() },
        series = response.series,
        poster = response.poster,
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
        playlist = response.playlist?.map { toDomain(response.id, it) },
        torrents = response.torrents?.map { toDomain(response.id, it) },
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

    fun toDomain(releaseId: Int, response: EpisodeResponse) = Episode(
        releaseId = releaseId,
        id = response.id,
        title = response.title,
        sd = response.sd,
        hd = response.hd,
        fullhd = response.fullhd,
        srcSd = response.srcSd,
        srcHd = response.srcHd
    )

    fun toDomain(releaseId: Int, response: TorrentResponse) = Torrent(
        releaseId = releaseId,
        id = response.id,
        hash = response.hash,
        leechers = response.leechers,
        seeders = response.seeders,
        completed = response.completed,
        quality = response.quality,
        series = response.series,
        size = response.size,
        time = response.time.dateFromSec(),
        url = response.url
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