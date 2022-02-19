package ru.radiationx.anilibria.presentation.main

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.InjectViewState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.data.analytics.profile.AnalyticsProfile
import ru.radiationx.data.datasource.holders.AppThemeHolder
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import tv.anilibria.module.data.AuthStateHolder
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.*
import tv.anilibria.module.data.repos.DonationRepository
import tv.anilibria.module.domain.entity.AuthState
import javax.inject.Inject

/**
 * Created by radiationx on 17.12.17.
 */
@InjectViewState
class MainPresenter @Inject constructor(
    private val router: Router,
    private val authStateHolder: AuthStateHolder,
    private val donationRepository: DonationRepository,
    private val appThemeHolder: AppThemeHolder,
    private val apiConfig: ApiConfig,
    private val schedulers: SchedulersProvider,
    private val analyticsProfile: AnalyticsProfile,
    private val authMainAnalytics: AuthMainAnalytics,
    private val catalogAnalytics: CatalogAnalytics,
    private val favoritesAnalytics: FavoritesAnalytics,
    private val feedAnalytics: FeedAnalytics,
    private val youtubeVideosAnalytics: YoutubeVideosAnalytics,
    private val otherAnalytics: OtherAnalytics
) : BasePresenter<MainView>(router) {

    var defaultScreen = Screens.MainFeed().screenKey!!

    private var firstLaunch = true

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        analyticsProfile.update()
        appThemeHolder
            .observeTheme()
            .subscribe { viewState.changeTheme(it) }
            .addToDisposable()

        apiConfig
            .observeNeedConfig()
            .distinctUntilChanged()
            .observeOn(schedulers.ui())
            .subscribe({
                if (it) {
                    viewState.showConfiguring()
                } else {
                    viewState.hideConfiguring()
                    if (firstLaunch) {
                        initMain()
                    }
                }
            }, {
                it.printStackTrace()
                throw it
            })
            .addToDisposable()

        if (apiConfig.needConfig) {
            viewState.showConfiguring()
        } else {
            initMain()
        }
    }

    private fun initMain() {
        firstLaunch = false
        viewModelScope.launch {
            if (authStateHolder.get() == AuthState.NO_AUTH) {
                authMainAnalytics.open(AnalyticsConstants.screen_main)
                router.navigateTo(Screens.Auth())
            }
        }


        selectTab(defaultScreen)
        authStateHolder
            .observe()
            .onEach { viewState.updateTabs() }
            .launchIn(viewModelScope)
        viewModelScope.launch {
            runCatching {
                donationRepository.requestUpdate()
            }.onFailure {
                it.printStackTrace()
            }
        }
        viewState.onMainLogicCompleted()
    }

    // todo remove blocking
    fun getAuthState() = runBlocking { authStateHolder.get() }

    fun selectTab(screenKey: String) {
        viewState.highlightTab(screenKey)
    }

    fun submitScreenAnalytics(screen: Screen) {
        when (screen) {
            is Screens.ReleasesSearch -> catalogAnalytics.open(AnalyticsConstants.screen_main)
            is Screens.Favorites -> favoritesAnalytics.open(AnalyticsConstants.screen_main)
            is Screens.MainFeed -> feedAnalytics.open(AnalyticsConstants.screen_main)
            is Screens.MainYouTube -> youtubeVideosAnalytics.open(AnalyticsConstants.screen_main)
            is Screens.MainOther -> otherAnalytics.open(AnalyticsConstants.screen_main)
        }
    }

}
