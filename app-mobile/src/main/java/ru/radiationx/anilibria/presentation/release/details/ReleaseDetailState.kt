package ru.radiationx.anilibria.presentation.release.details

import ru.radiationx.anilibria.model.DonationCardItemState

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
    val id: Int,
    val info: ReleaseInfoState,
    val episodesControl: ReleaseEpisodesControlState?,
    val episodesTabs: List<EpisodesTabState>,
    val torrents: List<ReleaseTorrentItemState>,
    val blockedInfo: ReleaseBlockedInfoState?
)

data class ReleaseInfoState(
    val titleRus: String,
    val titleEng: String,
    val description: String,
    val info: String,
    val days: List<Int>,
    val isOngoing: Boolean,
    val announce: String?,
    val favorite: ReleaseFavoriteState
)

data class ReleaseFavoriteState(
    val rating: String,
    val isAdded: Boolean
)

data class ReleaseEpisodeItemState(
    val id: Int,
    val releaseId: Int,
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
    val id: Int,
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