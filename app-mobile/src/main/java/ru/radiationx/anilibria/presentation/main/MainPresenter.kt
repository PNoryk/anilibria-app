package ru.radiationx.anilibria.presentation.main

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moxy.InjectViewState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.Screen
import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.feature.donation.data.DonationRepository
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.analytics.api.AnalyticsConstants
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.feature.analytics.api.features.*
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile
import javax.inject.Inject

@InjectViewState
@InjectConstructor
class MainPresenter(
    private val router: Router,
    private val authStateHolder: AuthStateHolder,
    private val donationRepository: DonationRepository,
    private val preferencesStorage: PreferencesStorage,
    private val apiConfig: ApiConfigController,
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
        preferencesStorage
            .appTheme.observe()
            .onEach { viewState.changeTheme(it) }
            .launchIn(viewModelScope)

        apiConfig
            .observeNeedConfig()
            .distinctUntilChanged()
            .onEach {
                if (it) {
                    viewState.showConfiguring()
                } else {
                    viewState.hideConfiguring()
                    if (firstLaunch) {
                        initMain()
                    }
                }
            }
            .launchIn(viewModelScope)

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
