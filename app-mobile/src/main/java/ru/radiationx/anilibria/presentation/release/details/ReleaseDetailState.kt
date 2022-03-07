package ru.radiationx.anilibria.presentation.release.details

import kotlinx.datetime.DayOfWeek
import ru.radiationx.anilibria.model.DonationCardItemState
import tv.anilibria.feature.domain.entity.release.EpisodeId
import tv.anilibria.feature.domain.entity.release.ReleaseId
import tv.anilibria.feature.domain.entity.release.TorrentId

data class ReleaseDetailScreenState(
    val data: ReleaseDetailState? = null,
    val modifiers: ReleaseDetailModifiersState = ReleaseDetailModifiersState(),
    val remindText: String? = null,
    val donationCardState: DonationCardItemState? = null
)

data class EpisodesTabState(
    val tag: String,
    val title: String,
    val textColor: Int?,
    val episodes: List<ReleaseEpisodeItemState>
)

data class ReleaseDetailModifiersState(
    val selectedEpisodesTabTag: String? = null,
    val favoriteRefreshing: Boolean = false,
    val episodesReversed: Boolean = false,
    val descriptionExpanded: Boolean = false
)

data class ReleaseDetailState(
    val id: ReleaseId,
    val info: ReleaseInfoState,
    val episodesControl: ReleaseEpisodesControlState?,
    val episodesTabs: List<EpisodesTabState>,
    val torrents: List<ReleaseTorrentItemState>,
    val blockedInfo: ReleaseBlockedInfoState?
)

data class ReleaseInfoState(
    val nameRus: String,
    val nameEng: String,
    val description: String,
    val info: String,
    val day: DayOfWeek?,
    val isOngoing: Boolean,
    val announce: String?,
    val favorite: ReleaseFavoriteState?
)

data class ReleaseFavoriteState(
    val rating: String,
    val isAdded: Boolean
)

data class ReleaseEpisodeItemState(
    val id: EpisodeId,
    val title: String,
    val subtitle: String?,
    val isViewed: Boolean,
    val hasSd: Boolean,
    val hasHd: Boolean,
    val hasFullHd: Boolean,
    val type: ReleaseEpisodeItemType,
    val tag: String,
    val actionTitle: String?,
    val actionColorRes: Int?,
    val actionIconRes: Int?,
    val hasActionUrl: Boolean
)

enum class ReleaseEpisodeItemType {
    ONLINE, SOURCE, EXTERNAL
}

data class ReleaseTorrentItemState(
    val id: TorrentId,
    val title: String,
    val subtitle: String,
    val size: String,
    val seeders: String,
    val leechers: String,
    val date: String?
)

data class ReleaseEpisodesControlState(
    val hasWeb: Boolean,
    val hasEpisodes: Boolean,
    val hasViewed: Boolean,
    val continueTitle: String
)

data class ReleaseBlockedInfoState(
    val title: String
)