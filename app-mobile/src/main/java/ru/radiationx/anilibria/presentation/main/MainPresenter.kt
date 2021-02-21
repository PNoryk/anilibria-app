package ru.radiationx.anilibria.presentation.main

import moxy.InjectViewState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import ru.radiationx.data.SchedulersProvider
import ru.radiationx.data.analytics.AnalyticsConstants
import ru.radiationx.data.analytics.features.AuthMainAnalytics
import ru.radiationx.data.analytics.profile.AnalyticsProfile
import ru.radiationx.data.datasource.holders.AppThemeHolder
import ru.radiationx.data.datasource.remote.address.ApiConfig
import ru.radiationx.data.entity.common.AuthState
import ru.radiationx.data.repository.AuthRepository
import ru.radiationx.data.system.LocaleHolder
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by radiationx on 17.12.17.
 */
@InjectViewState
class MainPresenter @Inject constructor(
        private val router: Router,
        private val systemMessenger: SystemMessenger,
        private val errorHandler: IErrorHandler,
        private val authRepository: AuthRepository,
        private val appThemeHolder: AppThemeHolder,
        private val apiConfig: ApiConfig,
        private val schedulers: SchedulersProvider,
        private val localeHolder: LocaleHolder,
        private val analyticsProfile: AnalyticsProfile,
        private val authMainAnalytics: AuthMainAnalytics
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
        if (authRepository.getAuthState() == AuthState.NO_AUTH) {
            authMainAnalytics.open(AnalyticsConstants.screen_main)
            router.navigateTo(Screens.Auth())
        }

        selectTab(defaultScreen)
        authRepository
                .observeUser()
                .subscribe {
                    viewState.updateTabs()
                }
                .addToDisposable()
        viewState.onMainLogicCompleted()
        authRepository
                .loadUser()
                .subscribe({}, {})
                .addToDisposable()
    }

    fun getAuthState() = authRepository.getAuthState()

    fun selectTab(screenKey: String) {
        viewState.highlightTab(screenKey)
    }

}
