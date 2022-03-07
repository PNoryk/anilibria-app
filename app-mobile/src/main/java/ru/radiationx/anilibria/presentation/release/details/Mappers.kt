package ru.radiationx.anilibria.presentation.release.details

import kotlinx.datetime.Instant
import ru.radiationx.anilibria.model.asDataColorRes
import ru.radiationx.anilibria.model.asDataIconRes
import ru.radiationx.shared.ktx.asTimeSecString
import ru.radiationx.shared_app.common.SystemUtils
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.content.types.release.*
import java.util.*

fun Release.toState(
    systemUtils: SystemUtils,
    episodesVisits: List<EpisodeVisit>
): ReleaseDetailState = ReleaseDetailState(
    id = id,
    info = toInfoState(),
    episodesControl = toEpisodeControlState(episodesVisits),
    episodesTabs = toTabsState(episodesVisits),
    torrents = torrents?.map { it.toState(systemUtils) }.orEmpty(),
    blockedInfo = blockedInfo?.takeIf { it.isBlocked }?.toState()
)

fun FavoriteInfo.toState() = ReleaseFavoriteState(
    rating = rating.toString(),
    isAdded = isAdded
)

fun Release.toInfoState(): ReleaseInfoState {
    val seasonsHtml = "<b>Год:</b> " + year?.value.orEmpty()
    val voicesHtml = "<b>Голоса:</b> " + voices?.joinToString(", ").orEmpty()
    val typesHtml = "<b>Тип:</b> " + type.orEmpty()
    val releaseStatus = status ?: "Не указано"
    val releaseStatusHtml = "<b>Состояние релиза:</b> $releaseStatus"
    val genresHtml =
        "<b>Жанр:</b> " + genres
            ?.map { it.value }
            ?.joinToString(", ") { "<a href=\"$it\">${it.capitalize()}</a>" }
    val arrHtml = arrayOf(
        seasonsHtml,
        voicesHtml,
        typesHtml,
        releaseStatusHtml,
        genresHtml
    )
    val infoStr = arrHtml.joinToString("<br>")

    return ReleaseInfoState(
        nameRus = nameRus?.text.orEmpty(),
        nameEng = nameEng?.text.orEmpty(),
        description = description?.text.orEmpty(),
        info = infoStr,
        day = scheduleDay,
        isOngoing = status == ReleaseStatus.ONGOING,
        announce = announce?.text,
        favorite = favoriteInfo?.toState()
    )
}

fun BlockedInfo.toState(): ReleaseBlockedInfoState {
    val defaultReason = """
                    <h4>Контент недоступен на территории Российской Федерации*. Приносим извинения за неудобства.</h4>
                    <br>
                    <span>Подробности смотрите в новостях или социальных сетях</span>""".trimIndent()

    return ReleaseBlockedInfoState(
        title = reason?.text ?: defaultReason
    )
}

fun Release.toEpisodeControlState(episodesVisits: List<EpisodeVisit>): ReleaseEpisodesControlState? {
    val hasEpisodes = episodesVisits.isNotEmpty()
    val hasViewed = episodesVisits.any { it.isViewed }
    val hasWeb = !webPlayerUrl?.value.isNullOrEmpty()
    val continueTitle = if (hasViewed) {
        val defaultInstant = Instant.fromEpochMilliseconds(0)
        val lastViewed = episodesVisits.maxByOrNull { it.lastOpenAt ?: defaultInstant }
        "Продолжить c ${lastViewed?.id?.id} серии"
    } else {
        "Начать просмотр"
    }

    return if (hasWeb || hasEpisodes) {
        ReleaseEpisodesControlState(
            hasWeb = hasWeb,
            hasEpisodes = hasEpisodes,
            hasViewed = hasViewed,
            continueTitle = continueTitle
        )
    } else {
        null
    }
}

fun Torrent.toState(systemUtils: SystemUtils): ReleaseTorrentItemState = ReleaseTorrentItemState(
    id = id,
    title = "Серия $series",
    subtitle = quality.orEmpty(),
    size = systemUtils.readableFileSize(size.value),
    seeders = seeders.toString(),
    leechers = leechers.toString(),
    date = null
)

fun Release.toTabsState(episodesVisits: List<EpisodeVisit>): List<EpisodesTabState> {
    val onlineTab = EpisodesTabState(
        tag = "online",
        title = "Онлайн",
        textColor = null,
        episodes = episodes?.map { episode ->
            val visit = episodesVisits.find { it.id == episode.id }
            episode.toOnlineState(visit)
        }.orEmpty()
    )
    val sourceTab = EpisodesTabState(
        tag = "source",
        title = "Скачать",
        textColor = null,
        episodes = episodes?.map { it.toSourceState() }.orEmpty()
    )
    val externalTabs = externalPlaylists?.map { it.toTabState() }.orEmpty()

    return listOf(onlineTab, sourceTab)
        .plus(externalTabs)
        .filter { tab ->
            tab.episodes.isNotEmpty()
                    && tab.episodes.all { it.hasSd || it.hasHd || it.hasFullHd || it.hasActionUrl }
        }
}

fun ExternalPlaylist.toTabState(): EpisodesTabState = EpisodesTabState(
    tag = tag,
    title = title,
    textColor = color.asDataColorRes(),
    episodes = episodes.map { it.toState(this) }
)

fun ExternalEpisode.toState(
    playlist: ExternalPlaylist
): ReleaseEpisodeItemState = ReleaseEpisodeItemState(
    id = id,
    title = title.orEmpty(),
    subtitle = null,
    isViewed = false,
    hasSd = false,
    hasHd = false,
    hasFullHd = false,
    type = ReleaseEpisodeItemType.EXTERNAL,
    tag = playlist.tag,
    actionTitle = playlist.actionText,
    hasActionUrl = url != null,
    actionIconRes = playlist.icon.asDataIconRes(),
    actionColorRes = playlist.color.asDataColorRes()
)

fun Episode.toSourceState(): ReleaseEpisodeItemState = ReleaseEpisodeItemState(
    id = id,
    title = title.orEmpty(),
    subtitle = null,
    isViewed = false,
    hasSd = urlSd != null,
    hasHd = urlHd != null,
    hasFullHd = urlFullHd != null,
    type = ReleaseEpisodeItemType.SOURCE,
    tag = "source",
    actionTitle = null,
    hasActionUrl = false,
    actionIconRes = null,
    actionColorRes = null
)

fun Episode.toOnlineState(visit: EpisodeVisit?): ReleaseEpisodeItemState {
    val subtitle = if (visit?.isViewed == true && visit.playerSeek ?: 0 > 0) {
        "Остановлена на ${Date(visit.playerSeek ?: 0).asTimeSecString()}"
    } else {
        null
    }
    return ReleaseEpisodeItemState(
        id = id,
        title = title.orEmpty(),
        subtitle = subtitle,
        isViewed = visit?.isViewed == true,
        hasSd = urlSd != null,
        hasHd = urlHd != null,
        hasFullHd = urlFullHd != null,
        type = ReleaseEpisodeItemType.ONLINE,
        tag = "online",
        actionTitle = null,
        hasActionUrl = false,
        actionIconRes = null,
        actionColorRes = null
    )
}