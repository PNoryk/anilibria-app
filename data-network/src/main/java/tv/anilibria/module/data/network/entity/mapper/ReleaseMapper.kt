package tv.anilibria.module.data.network.entity.mapper

import kotlinx.datetime.Instant
import tv.anilibria.module.data.network.entity.app.release.*
import tv.anilibria.module.domain.entity.common.asAbsoluteUrl
import tv.anilibria.module.domain.entity.common.asBytes
import tv.anilibria.module.domain.entity.common.asHtmlText
import tv.anilibria.module.domain.entity.common.asRelativeUrl
import tv.anilibria.module.domain.entity.release.*

fun RandomReleaseResponse.toDomain() = RandomRelease(code = code)

fun ReleaseResponse.toDomain() = Release(
    id = id,
    code = code,
    names = names?.map { it.asHtmlText() },
    series = series,
    poster = poster?.asRelativeUrl(),
    torrentUpdate = torrentUpdate?.let { Instant.fromEpochSeconds(it) },
    statusName = status,
    status = statusCode,
    type = type,
    genres = genres,
    voices = voices,
    year = year,
    season = season,
    scheduleDay = scheduleDay,
    description = description?.asHtmlText(),
    announce = announce?.asHtmlText(),
    favoriteInfo = favoriteInfo?.toDomain(),
    showDonateDialog = showDonateDialog,
    blockedInfo = blockedInfo?.toDomain(),
    webPlayerUrl = moonwalkLink?.asAbsoluteUrl(),
    episodes = episodes?.map { it.toDomain() },
    externalPlaylists = externalPlaylists?.map { it.toDomain() },
    torrents = torrents?.map { it.toDomain() }
)

fun FavoriteInfoResponse.toDomain() = FavoriteInfo(
    rating = rating,
    isAdded = isAdded
)

fun BlockedInfoResponse.toDomain() = BlockedInfo(
    isBlocked = isBlocked,
    reason = reason?.asHtmlText()
)

fun EpisodeResponse.toDomain() = Episode(
    id = id,
    title = title,
    urlSd = urlSd?.asAbsoluteUrl(),
    urlHd = urlHd?.asAbsoluteUrl(),
    urlFullHd = urlFullHd?.asAbsoluteUrl(),
    srcUrlSd = srcUrlSd?.asAbsoluteUrl(),
    srcUrlHd = srcUrlHd?.asAbsoluteUrl(),
    srcUrlFullHd = srcUrlFullHd?.asAbsoluteUrl()
)

fun ExternalPlaylistResponse.toDomain() = ExternalPlaylist(
    tag = tag,
    title = title,
    actionText = actionText,
    episodes = episodes.map { it.toDomain() }
)

fun ExternalEpisodeResponse.toDomain() = ExternalEpisode(
    id = id,
    title = title,
    url = url?.asAbsoluteUrl()
)

fun TorrentResponse.toDomain() = Torrent(
    id = id,
    hash = hash,
    leechers = leechers,
    seeders = seeders,
    completed = completed,
    quality = quality,
    series = series,
    size = size.asBytes(),
    url = url?.asRelativeUrl()
)
