package tv.anilibria.feature.content.data.remote.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.core.types.*
import tv.anilibria.feature.content.data.remote.entity.app.release.*
import tv.anilibria.feature.content.types.ReleaseGenre
import tv.anilibria.feature.content.types.ReleaseSeason
import tv.anilibria.feature.content.types.ReleaseYear
import tv.anilibria.feature.content.types.other.toDataColor
import tv.anilibria.feature.content.types.other.toDataIcon
import tv.anilibria.feature.content.types.release.*

fun RandomReleaseResponse.toDomain() = RandomRelease(
    code = ReleaseCode(code)
)

fun ReleaseResponse.toDomain(): Release {
    val releaseId = ReleaseId(id)
    return Release(
        id = releaseId,
        code = code?.let { ReleaseCode(it) },
        names = names?.map { it.asHtmlText() },
        series = series,
        poster = poster?.asRelativeUrl(),
        torrentUpdate = torrentUpdate?.let { Instant.fromEpochSeconds(it) },
        statusName = status,
        status = statusCode?.toReleaseStatus(),
        type = type,
        genres = genres?.map { ReleaseGenre(it) },
        voices = voices,
        year = year?.let { ReleaseYear(it) },
        season = season?.let { ReleaseSeason(it) },
        scheduleDay = scheduleDay?.asWeekDay(),
        description = description?.asHtmlText(),
        announce = announce?.asHtmlText(),
        favoriteInfo = favoriteInfo?.toDomain(),
        showDonateDialog = showDonateDialog,
        blockedInfo = blockedInfo?.toDomain(),
        webPlayerUrl = moonwalkLink?.asAbsoluteUrl(),
        episodes = episodes?.map { it.toDomain(releaseId) },
        externalPlaylists = externalPlaylists?.map { it.toDomain(releaseId) },
        torrents = torrents?.map { it.toDomain(releaseId) }
    )
}

fun FavoriteInfoResponse.toDomain() = FavoriteInfo(
    rating = rating.asCount(),
    isAdded = isAdded
)

fun BlockedInfoResponse.toDomain() = BlockedInfo(
    isBlocked = isBlocked,
    reason = reason?.asHtmlText()
)

fun EpisodeResponse.toDomain(releaseId: ReleaseId) = Episode(
    id = EpisodeId(id, releaseId),
    title = title,
    urlSd = urlSd?.asAbsoluteUrl(),
    urlHd = urlHd?.asAbsoluteUrl(),
    urlFullHd = urlFullHd?.asAbsoluteUrl(),
    srcUrlSd = srcUrlSd?.asAbsoluteUrl(),
    srcUrlHd = srcUrlHd?.asAbsoluteUrl(),
    srcUrlFullHd = srcUrlFullHd?.asAbsoluteUrl()
)

fun ExternalPlaylistResponse.toDomain(releaseId: ReleaseId) = ExternalPlaylist(
    tag = tag,
    title = title,
    actionText = actionText,
    episodes = episodes.map { it.toDomain(releaseId) },
    color = tag.toDataColor(),
    icon = tag.toDataIcon()
)

fun ExternalEpisodeResponse.toDomain(releaseId: ReleaseId) = ExternalEpisode(
    id = EpisodeId(id, releaseId),
    title = title,
    url = url?.asAbsoluteUrl()
)

fun TorrentResponse.toDomain(releaseId: ReleaseId) = Torrent(
    id = TorrentId(id, releaseId),
    hash = hash,
    leechers = leechers.asCount(),
    seeders = seeders.asCount(),
    completed = completed.asCount(),
    quality = quality,
    series = series,
    size = size.asBytes(),
    url = url?.asRelativeUrl()
)

fun String.toReleaseStatus(): ReleaseStatus = when (this) {
    "1" -> ReleaseStatus.ONGOING
    "2" -> ReleaseStatus.COMPLETE
    "3" -> ReleaseStatus.HIDDEN
    "4" -> ReleaseStatus.NOT_ONGOING
    else -> ReleaseStatus.UNKNOWN
}

