package tv.anilibria.module.data.network.entity.mapper

import tv.anilibria.module.data.network.entity.app.release.*
import tv.anilibria.module.domain.entity.release.*

fun ReleaseResponse.toDomain() = Release(
    id = id,
    code = code,
    names = names,
    series = series,
    poster = poster,
    torrentUpdate = torrentUpdate,
    status = status,
    statusCode = statusCode,
    type = type,
    genres = genres,
    voices = voices,
    year = year,
    season = season,
    scheduleDay = scheduleDay,
    description = description,
    announce = announce,
    favoriteInfo = favoriteInfo?.toDomain(),
    showDonateDialog = showDonateDialog,
    blockedInfo = blockedInfo?.toDomain(),
    moonwalkLink = moonwalkLink,
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
    reason = reason
)

fun EpisodeResponse.toDomain() = Episode(
    id = id,
    title = title,
    urlSd = urlSd,
    urlHd = urlHd,
    urlFullHd = urlFullHd,
    srcUrlSd = srcUrlSd,
    srcUrlHd = srcUrlHd,
    srcUrlFullHd = srcUrlFullHd
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
    url = url
)

fun TorrentResponse.toDomain() = Torrent(
    id = id,
    hash = hash,
    leechers = leechers,
    seeders = seeders,
    completed = completed,
    quality = quality,
    series = series,
    size = size,
    url = url
)
