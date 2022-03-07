package ru.radiationx.anilibria.presentation.feed

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import moxy.InjectViewState
import ru.radiationx.anilibria.model.*
import ru.radiationx.anilibria.model.loading.DataLoadingController
import ru.radiationx.anilibria.model.loading.PageLoadParams
import ru.radiationx.anilibria.model.loading.ScreenStateAction
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.fragments.feed.FeedDataState
import ru.radiationx.anilibria.ui.fragments.feed.FeedScheduleState
import ru.radiationx.anilibria.ui.fragments.feed.FeedScreenState
import ru.radiationx.anilibria.utils.ShortcutHelper
import ru.radiationx.shared.ktx.asDayNameDeclension
import ru.radiationx.shared.ktx.asDayPretext
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.appupdates.data.CheckerRepository
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.feature.content.data.BaseUrlHelper
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.*
import tv.anilibria.feature.content.data.preferences.PreferencesStorage
import tv.anilibria.feature.content.data.repos.FeedRepository
import tv.anilibria.feature.content.data.repos.ScheduleRepository
import tv.anilibria.feature.domain.entity.feed.Feed
import tv.anilibria.feature.domain.entity.release.Release
import tv.anilibria.feature.domain.entity.release.ReleaseId
import tv.anilibria.feature.domain.entity.youtube.Youtube
import tv.anilibria.feature.domain.entity.youtube.YoutubeId
import tv.anilibria.plugin.shared.appinfo.SharedBuildConfig
import javax.inject.Inject

/* Created by radiationx on 05.11.17. */

@InjectViewState
class FeedPresenter @Inject constructor(
    private val feedRepository: FeedRepository,
    private val releaseInteractor: ReleaseInteractor,
    private val scheduleRepository: ScheduleRepository,
    private val checkerRepository: CheckerRepository,
    private val sharedBuildConfig: SharedBuildConfig,
    private val preferencesStorage: PreferencesStorage,
    private val donationRepository: DonationRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val fastSearchAnalytics: FastSearchAnalytics,
    private val feedAnalytics: FeedAnalytics,
    private val scheduleAnalytics: ScheduleAnalytics,
    private val youtubeAnalytics: YoutubeAnalytics,
    private val releaseAnalytics: ReleaseAnalytics,
    private val updaterAnalytics: UpdaterAnalytics,
    private val donationDetailAnalytics: DonationDetailAnalytics,
    private val donationCardAnalytics: DonationCardAnalytics,
    private val shortcutHelper: ShortcutHelper,
    private val appLinkHelper: AppLinkHelper,
    private val urlHelper: BaseUrlHelper
) : BasePresenter<FeedView>(router) {

    companion object {
        private const val DONATION_NEW_TAG = "donation_new"
    }

    private val loadingController = DataLoadingController(viewModelScope) {
        submitPageAnalytics(it.page)
        getDataSource(it)
    }

    private val stateController = StateController(FeedScreenState())

    private var randomJob: Job? = null

    private var lastLoadedPage: Int? = null

    private val currentItems = mutableListOf<Feed>()
    private val currentScheduleItems = mutableListOf<Release>()

    private var appUpdateNeedClose = false
    private var hasAppUpdate = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        checkerRepository
            .observeUpdate()
            .onEach {
                hasAppUpdate = it.code > sharedBuildConfig.versionCode
                updateAppUpdateState()
            }
            .launchIn(viewModelScope)

        preferencesStorage
            .newDonationRemind
            .observe()
            .flatMapConcat { enabled ->
                donationRepository.observerDonationInfo().map {
                    Pair(it.cardNewDonations, enabled)
                }
            }
            .onEach { pair ->
                val newDonationState = if (pair.second) {
                    pair.first?.let {
                        DonationCardItemState(
                            tag = DONATION_NEW_TAG,
                            title = it.title,
                            subtitle = it.subtitle,
                            canClose = false
                        )
                    }
                } else {
                    null
                }
                stateController.updateState {
                    it.copy(donationCardItemState = newDonationState)
                }
            }
            .launchIn(viewModelScope)

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        loadingController.observeState()
            .onEach { loadingState ->
                stateController.updateState {
                    it.copy(data = loadingState)
                }
            }
            .launchIn(viewModelScope)

        loadingController.refresh()
    }

    fun refreshReleases() {
        loadingController.refresh()
    }

    fun loadMore() {
        loadingController.loadMore()
    }

    fun onScheduleScroll(position: Int) {
        feedAnalytics.scheduleHorizontalScroll(position)
    }

    fun onScheduleItemClick(item: ScheduleItemState, position: Int) {
        val releaseItem = currentScheduleItems
            .find { it.id == item.releaseId }
            ?: return
        feedAnalytics.scheduleReleaseClick(position)
        releaseAnalytics.open(AnalyticsConstants.screen_feed, releaseItem.id.id)
        router.navigateTo(
            Screens.ReleaseDetails(releaseItem.id, releaseItem.code)
        )
    }

    fun onItemClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        feedAnalytics.releaseClick()
        releaseAnalytics.open(AnalyticsConstants.screen_feed, releaseItem.id.id)
        router.navigateTo(Screens.ReleaseDetails(releaseItem.id, releaseItem.code))
    }

    fun onYoutubeClick(item: YoutubeItemState) {
        val youtubeItem = findYoutube(item.id) ?: return
        youtubeAnalytics.openVideo(
            AnalyticsConstants.screen_feed,
            youtubeItem.id.id,
            youtubeItem.vid?.id
        )
        feedAnalytics.youtubeClick()
        appLinkHelper.openLink(youtubeItem.link)
    }

    fun onSchedulesClick() {
        scheduleAnalytics.open(AnalyticsConstants.screen_feed)
        feedAnalytics.scheduleClick()
        router.navigateTo(Screens.Schedule())
    }

    fun onRandomClick() {
        feedAnalytics.randomClick()
        if (randomJob?.isActive == true) {
            return
        }
        randomJob = viewModelScope.launch {
            runCatching {
                releaseInteractor.getRandomRelease()
            }.onSuccess {
                releaseAnalytics.open(AnalyticsConstants.screen_feed, null, it.code.code)
                router.navigateTo(Screens.ReleaseDetails(code = it.code))
            }.onFailure {
                errorHandler.handle(it)
            }
        }
    }

    fun onFastSearchOpen() {
        fastSearchAnalytics.open(AnalyticsConstants.screen_feed)
    }

    fun appUpdateClick() {
        updaterAnalytics.appUpdateCardClick()
        router.navigateTo(Screens.AppUpdateScreen(false, AnalyticsConstants.app_update_card))
    }

    fun appUpdateCloseClick() {
        updaterAnalytics.appUpdateCardCloseClick()
        appUpdateNeedClose = true
        updateAppUpdateState()
    }

    fun onDonationClick(state: DonationCardItemState) {
        when (state.tag) {
            DONATION_NEW_TAG -> {
                donationCardAnalytics.onNewDonationClick(AnalyticsConstants.screen_feed)
                preferencesStorage.newDonationRemind.blockingSet(false)
            }
        }
        donationDetailAnalytics.open(AnalyticsConstants.screen_feed)
        router.navigateTo(Screens.DonationDetail())
    }

    fun onDonationCloseClick(state: DonationCardItemState) {
        when (state.tag) {
            DONATION_NEW_TAG -> {
                donationCardAnalytics.onNewDonationCloseClick(AnalyticsConstants.screen_feed)
                preferencesStorage.newDonationRemind.blockingSet(false)
            }
        }
    }

    private fun updateAppUpdateState() {
        stateController.updateState {
            it.copy(hasAppUpdate = hasAppUpdate && !appUpdateNeedClose)
        }
    }

    fun onCopyClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.copyLink(releaseItem.link)
        releaseAnalytics.copyLink(AnalyticsConstants.screen_feed, item.id.id)
    }

    fun onShareClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        appLinkHelper.shareLink(releaseItem.link)
        releaseAnalytics.share(AnalyticsConstants.screen_feed, item.id.id)
    }

    fun onShortcutClick(item: ReleaseItemState) {
        val releaseItem = findRelease(item.id) ?: return
        shortcutHelper.addShortcut(releaseItem)
        releaseAnalytics.shortcut(AnalyticsConstants.screen_feed, item.id.id)
    }

    private fun findRelease(id: ReleaseId): Release? {
        return currentItems.mapNotNull { it.release }.firstOrNull { it.id == id }
    }

    private fun findYoutube(id: YoutubeId): Youtube? {
        return currentItems.mapNotNull { it.youtube }.firstOrNull { it.id == id }
    }

    private fun submitPageAnalytics(page: Int) {
        if (lastLoadedPage != page) {
            feedAnalytics.loadPage(page)
            lastLoadedPage = page
        }
    }

    private suspend fun getFeedSource(page: Int): List<Feed> = feedRepository
        .getFeed(page)
        .also {
            if (page == 1) {
                currentItems.clear()
            }
            currentItems.addAll(it)
        }

    private suspend fun getScheduleSource(): FeedScheduleState = scheduleRepository
        .loadSchedule()
        .let { scheduleDays ->
            val mskDay = Clock.System.todayAt(TimeZone.of("MSK")).dayOfWeek
            val currentDay = Clock.System.todayAt(TimeZone.currentSystemDefault()).dayOfWeek

            val dayTitle = if (currentDay == mskDay) {
                "Ожидается сегодня"
            } else {
                val preText = mskDay.asDayPretext()
                val dayName = mskDay.asDayNameDeclension().toLowerCase()
                "Ожидается $preText $dayName (по МСК)"
            }

            currentScheduleItems.clear()
            val items = scheduleDays
                .firstOrNull { it.day == mskDay }
                ?.items
                ?.also { currentScheduleItems.addAll(it) }
                ?.map { it.toScheduleState(urlHelper) }
                .orEmpty()

            FeedScheduleState(dayTitle, items)
        }


    private suspend fun getDataSource(params: PageLoadParams): ScreenStateAction.Data<FeedDataState> {
        return try {
            val feedItems = getFeedSource(params.page)
            val scheduleState = if (params.isFirstPage) {
                getScheduleSource()
            } else {
                loadingController.currentState.data?.schedule ?: getScheduleSource()
            }

            val feedDataState = FeedDataState(
                feedItems = currentItems.map { it.toState(urlHelper) },
                schedule = scheduleState
            )
            ScreenStateAction.Data(feedDataState, feedItems.isNotEmpty())
        } catch (ex: Exception) {
            if (params.isFirstPage) {
                errorHandler.handle(ex)
            }
            throw ex
        }
    }
}
