package ru.radiationx.anilibria.presentation.release.details

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import moxy.InjectViewState
import ru.radiationx.anilibria.model.DonationCardItemState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.presentation.common.ILinkHandler
import ru.radiationx.anilibria.ui.adapters.release.detail.EpisodeControlPlace
import ru.radiationx.shared_app.AppLinkHelper
import ru.radiationx.shared_app.common.SystemUtils
import ru.terrakok.cicerone.Router
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerQuality
import tv.anilibria.feature.player.data.prefs.PrefferedPlayerType
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.*
import tv.anilibria.feature.content.data.analytics.features.mapper.toAnalyticsQuality
import tv.anilibria.feature.content.data.analytics.features.model.AnalyticsPlayer
import tv.anilibria.feature.content.data.analytics.features.model.AnalyticsQuality
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.feature.player.data.EpisodeHistoryRepository
import tv.anilibria.feature.content.data.repos.FavoriteRepository
import tv.anilibria.feature.content.data.repos.HistoryRepository
import tv.anilibria.feature.player.data.domain.EpisodeVisit
import tv.anilibria.feature.content.types.release.*
import javax.inject.Inject

@InjectViewState
class ReleaseInfoPresenter @Inject constructor(
    private val releaseInteractor: ReleaseInteractor,
    private val historyRepository: HistoryRepository,
    private val episodeHistoryRepository: EpisodeHistoryRepository,
    private val authStateHolder: AuthStateHolder,
    private val favoriteRepository: FavoriteRepository,
    private val donationRepository: DonationRepository,
    private val router: Router,
    private val linkHandler: ILinkHandler,
    private val errorHandler: IErrorHandler,
    private val preferencesStorage: PreferencesStorage,
    private val playerPreferencesStorage: PlayerPreferencesStorage,
    private val authMainAnalytics: AuthMainAnalytics,
    private val catalogAnalytics: CatalogAnalytics,
    private val scheduleAnalytics: ScheduleAnalytics,
    private val webPlayerAnalytics: WebPlayerAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val playerAnalytics: PlayerAnalytics,
    private val donationDetailAnalytics: DonationDetailAnalytics,
    private val appLinkHelper: AppLinkHelper,
    private val urlHelper: BaseUrlHelper,
    private val systemUtils: SystemUtils
) : BasePresenter<ReleaseInfoView>(router) {

    private val remindText =
        "Если серии всё ещё нет в плеере, воспользуйтесь торрентом или веб-плеером"

    private var currentData: ReleaseInfo? = null
    var releaseId: ReleaseId? = null
    var releaseIdCode: ReleaseCode? = null

    private val stateController = StateController(
        ReleaseDetailScreenState()
    )

    private fun updateModifiers(block: (ReleaseDetailModifiersState) -> ReleaseDetailModifiersState) {
        stateController.updateState {
            it.copy(
                modifiers = block.invoke(it.modifiers)
            )
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        donationRepository
            .observerDonationInfo()
            .onEach { info ->
                stateController.updateState { state ->
                    val newCardState = info.cardRelease?.let {
                        DonationCardItemState(
                            tag = "donate",
                            title = it.title,
                            subtitle = it.subtitle,
                            canClose = false
                        )
                    }
                    state.copy(donationCardState = newCardState)
                }
            }
            .launchIn(viewModelScope)

        preferencesStorage
            .episodesIsReverse.observe()
            .onEach { episodesReversed ->
                updateModifiers {
                    it.copy(episodesReversed = episodesReversed)
                }

            }
            .launchIn(viewModelScope)

        preferencesStorage
            .releaseRemind.observe()
            .onEach { remindEnabled ->
                stateController.updateState {
                    it.copy(remindText = remindText.takeIf { remindEnabled })
                }
            }
            .launchIn(viewModelScope)

        observeRelease()
        loadRelease()
        subscribeAuth()
    }

    fun getQuality() = playerPreferencesStorage.quality.blockingGet()

    fun setQuality(value: PrefferedPlayerQuality) =
        playerPreferencesStorage.quality.blockingSet(value)

    fun getPlayerType() = playerPreferencesStorage.playerType.blockingGet()

    fun setPlayerType(value: PrefferedPlayerType) =
        playerPreferencesStorage.playerType.blockingSet(value)


    private fun subscribeAuth() {
        authStateHolder
            .observe()
            .distinctUntilChanged()
            .drop(1)
            .onEach {
                loadRelease()
            }
            .launchIn(viewModelScope)
    }

    private fun loadRelease() {
        releaseInteractor
            .loadRelease(releaseId, releaseIdCode)
            .take(1)
            .onEach { historyRepository.putRelease(it.id) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)
    }

    private fun observeRelease() {
        releaseInteractor
            .observeFull(releaseId, releaseIdCode)
            .flatMapLatest { release ->
                episodeHistoryRepository
                    .observeByRelease(release.id)
                    .map { visits ->
                        ReleaseInfo(release.id, release, visits)
                    }
            }
            .onEach { updateLocalRelease(it) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)
    }

    private fun updateLocalRelease(releaseInfo: ReleaseInfo) {
        currentData = releaseInfo
        releaseId = releaseInfo.id
        releaseIdCode = releaseInfo.release.code
        stateController.updateState {
            it.copy(data = releaseInfo.release.toState(systemUtils, releaseInfo.episodeVisits))
        }
    }

    fun markEpisodeViewed(episode: Episode) {
        viewModelScope.launch {
            episodeHistoryRepository.markViewed(episode.id)
        }
    }

    fun markEpisodeUnviewed(episode: Episode) {
        releaseAnalytics.historyResetEpisode()
        viewModelScope.launch {
            episodeHistoryRepository.markUnViewed(episode.id)
        }
    }

    fun onEpisodeTabClick(tabTag: String) {
        currentData?.let { release ->
            releaseAnalytics.episodesTabClick(release.id.id, tabTag)
        }
        updateModifiers {
            it.copy(selectedEpisodesTabTag = tabTag)
        }
    }

    fun onRemindCloseClick() {
        preferencesStorage.releaseRemind.blockingSet(false)
    }

    fun onTorrentClick(item: ReleaseTorrentItemState) {
        currentData?.let { data ->
            val torrentItem = data.release.torrents?.find { it.id == item.id } ?: return
            val isHevc = torrentItem.quality?.contains("HEVC", true) == true
            releaseAnalytics.torrentClick(isHevc, data.id.id)
            urlHelper.makeMedia(torrentItem.url)?.also {
                appLinkHelper.openLink(it)
            }
        }
    }

    fun onCommentsClick() {
        currentData?.also { data ->
            releaseAnalytics.commentsClick(data.id.id)
        }
    }

    fun onClickWatchWeb(place: EpisodeControlPlace) {
        currentData?.let { data ->
            releaseAnalytics.webPlayerClick(data.id.id)
            val webUrl = data.release.webPlayerUrl
            val releaseLink = data.release.link
            if (webUrl != null && releaseLink != null) {
                viewState.playWeb(webUrl, releaseLink)
            }
        }
    }

    fun onPlayAllClick(place: EpisodeControlPlace) {
        currentData?.let { data ->
            place.handle({
                releaseAnalytics.episodesTopStartClick(data.id.id)
            }, {
                releaseAnalytics.episodesStartClick(data.id.id)
            })
            viewState.playEpisodes(data.release)
        }
    }

    fun onClickContinue(place: EpisodeControlPlace) {
        currentData?.also { data ->
            place.handle({
                releaseAnalytics.episodesTopContinueClick(data.id.id)
            }, {
                releaseAnalytics.episodesContinueClick(data.id.id)
            })
            val defaultInstant = Instant.fromEpochMilliseconds(0)
            data.episodeVisits
                .maxByOrNull { it.lastOpenAt ?: defaultInstant }
                ?.let { visit ->
                    data.release.episodes?.find { it.id == visit.id }
                }
                ?.let { episode ->
                    viewState.playContinue(data.release, episode)
                }
        }
    }

    fun submitPlayerOpenAnalytics(playerType: AnalyticsPlayer, quality: AnalyticsQuality) {
        playerAnalytics.open(AnalyticsConstants.screen_release, playerType, quality)
    }

    fun onClickEpisodesMenu(place: EpisodeControlPlace) {
        currentData?.also { viewState.showEpisodesMenuDialog() }
    }

    private fun onExternalEpisodeClick(
        episodeState: ReleaseEpisodeItemState,
        release: Release,
        episode: ExternalEpisode
    ) {
        releaseAnalytics.episodeExternalClick(release.id.id, episodeState.tag)
        appLinkHelper.openLink(episode.url)
    }

    private fun onSourceEpisodeClick(
        release: Release,
        episode: Episode,
        quality: PrefferedPlayerQuality? = null
    ) {
        val analyticsQuality = quality?.toAnalyticsQuality() ?: AnalyticsQuality.NONE
        releaseAnalytics.episodeDownloadClick(analyticsQuality, release.id.id)
        viewState.downloadEpisode(episode, quality)
    }

    private fun onOnlineEpisodeClick(
        release: Release,
        episode: Episode,
        playFlag: Int? = null,
        quality: PrefferedPlayerQuality? = null
    ) {
        val analyticsQuality = quality?.toAnalyticsQuality() ?: AnalyticsQuality.NONE
        releaseAnalytics.episodePlayClick(analyticsQuality, release.id.id)
        viewState.playEpisode(release, episode, playFlag, quality)
    }

    fun onEpisodeClick(
        episodeState: ReleaseEpisodeItemState,
        playFlag: Int? = null,
        quality: PrefferedPlayerQuality? = null
    ) {
        val release = currentData?.release ?: return
        when (episodeState.type) {
            ReleaseEpisodeItemType.ONLINE -> {
                val episodeItem = getEpisodeItem(episodeState) ?: return
                onOnlineEpisodeClick(release, episodeItem, playFlag, quality)
            }
            ReleaseEpisodeItemType.SOURCE -> {
                val episodeItem = getEpisodeItem(episodeState) ?: return
                onSourceEpisodeClick(release, episodeItem, quality)
            }
            ReleaseEpisodeItemType.EXTERNAL -> {
                val episodeItem = getExternalEpisode(episodeState) ?: return
                onExternalEpisodeClick(episodeState, release, episodeItem)
            }
        }
    }

    fun onLongClickEpisode(episode: ReleaseEpisodeItemState) {
        val episodeItem = getEpisodeItem(episode) ?: return
        viewState.showLongPressEpisodeDialog(episodeItem)
    }

    private fun getEpisodeItem(episode: ReleaseEpisodeItemState): Episode? {
        if (
            episode.type != ReleaseEpisodeItemType.ONLINE
            || episode.type != ReleaseEpisodeItemType.SOURCE
        ) {
            return null
        }
        return currentData?.release?.episodes?.find { it.id == episode.id }
    }

    private fun getExternalEpisode(episode: ReleaseEpisodeItemState): ExternalEpisode? {
        if (episode.type != ReleaseEpisodeItemType.EXTERNAL) return null
        val release = currentData?.release ?: return null
        return release.externalPlaylists
            ?.find { it.tag == episode.tag }
            ?.episodes
            ?.find { it.id == episode.id }
    }

    fun onClickLink(url: String) {
        currentData?.also { data ->
            releaseAnalytics.descriptionLinkClick(data.id.id)
            val handled = linkHandler.handle(url, router)
            if (!handled) {
                appLinkHelper.openLink(AbsoluteUrl(url))
            }
        }
    }

    fun onClickDonate() {
        currentData?.also {
            releaseAnalytics.donateClick(it.id.id)
            donationDetailAnalytics.open(AnalyticsConstants.screen_release)
            router.navigateTo(Screens.DonationDetail())
        }
    }

    fun onClickFav() {
        viewModelScope.launch {
            if (authStateHolder.get() != AuthState.AUTH) {
                viewState.showFavoriteDialog()
                return@launch
            }
            val releaseId = currentData?.id ?: return@launch
            val favInfo = currentData?.release?.favoriteInfo ?: return@launch

            if (favInfo.isAdded) {
                releaseAnalytics.favoriteRemove(releaseId.id)
            } else {
                releaseAnalytics.favoriteAdd(releaseId.id)
            }

            updateModifiers {
                it.copy(favoriteRefreshing = true)
            }

            runCatching {
                if (favInfo.isAdded) {
                    favoriteRepository.deleteFavorite(releaseId)
                } else {
                    favoriteRepository.addFavorite(releaseId)
                }
            }.onSuccess { releaseItem ->
                stateController.updateState {
                    it.copy(
                        data = it.data?.copy(
                            info = it.data.info.copy(
                                favorite = releaseItem.favoriteInfo?.toState()
                            )
                        )
                    )
                }
            }.onFailure {
                errorHandler.handle(it)
            }

            updateModifiers {
                it.copy(favoriteRefreshing = false)
            }
        }
    }

    fun onScheduleClick(day: DayOfWeek) {
        currentData?.also { data ->
            releaseAnalytics.scheduleClick(data.id.id)
        }
        scheduleAnalytics.open(AnalyticsConstants.screen_release)
        router.navigateTo(Screens.Schedule(day))
    }

    fun onDescriptionExpandClick() {
        updateModifiers {
            it.copy(descriptionExpanded = !it.descriptionExpanded)
        }
        currentData?.also { data ->
            if (stateController.currentState.modifiers.descriptionExpanded) {
                releaseAnalytics.descriptionExpand(data.id.id)
            }
        }
    }

    fun openAuth() {
        authMainAnalytics.open(AnalyticsConstants.screen_release)
        router.navigateTo(Screens.Auth())
    }

    fun openSearch(tag: String) {
        currentData?.also { data ->
            releaseAnalytics.genreClick(data.id.id)
            val genre = data.release.genres?.find { it.value == tag }
            catalogAnalytics.open(AnalyticsConstants.screen_release)
            router.navigateTo(Screens.ReleasesSearch(genre))
        }
    }

    fun onDownloadLinkSelected(url: AbsoluteUrl?) {
        currentData?.also { data ->
            if (data.release.showDonateDialog == true) {
                viewState.showFileDonateDialog(url)
            } else {
                viewState.showDownloadDialog(url)
            }
        }
    }

    fun submitDownloadEpisodeUrlAnalytics() {
        currentData?.also { data ->
            releaseAnalytics.episodeDownloadByUrl(data.id.id)
        }
    }

    fun onDialogPatreonClick() {
        appLinkHelper.openLink(AbsoluteUrl("https://www.patreon.com/anilibria"))
    }

    fun onDialogDonateClick() {
        router.navigateTo(Screens.DonationDetail())
    }

    fun onResetEpisodesHistoryClick() {
        releaseAnalytics.historyReset()
        currentData?.also { data ->
            viewModelScope.launch {
                episodeHistoryRepository.resetByRelease(data.id)
            }
        }
    }

    fun onCheckAllEpisodesHistoryClick() {
        releaseAnalytics.historyViewAll()
        currentData?.also { data ->
            viewModelScope.launch {
                episodeHistoryRepository.viewAllInRelease(data.release.episodes?.map { it.id }
                    .orEmpty())
            }
        }
    }

    fun onWebPlayerClick() {
        currentData?.also {
            webPlayerAnalytics.open(AnalyticsConstants.screen_release, it.id.id)
        }
    }

    private fun EpisodeControlPlace.handle(topListener: () -> Unit, bottomListener: () -> Unit) {
        when (this) {
            EpisodeControlPlace.TOP -> topListener()
            EpisodeControlPlace.BOTTOM -> bottomListener()
        }
    }

    private data class ReleaseInfo(
        val id: ReleaseId,
        val release: Release,
        val episodeVisits: List<EpisodeVisit>
    )
}