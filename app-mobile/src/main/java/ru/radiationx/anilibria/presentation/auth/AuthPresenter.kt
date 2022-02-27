package ru.radiationx.anilibria.presentation.auth

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.model.SocialAuthItemState
import ru.radiationx.anilibria.model.toState
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import ru.radiationx.shared_app.AppLinkHelper
import ru.terrakok.cicerone.Router
import tv.anilibria.core.types.RelativeUrl
import tv.anilibria.feature.auth.data.AuthRepository
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.module.data.analytics.AnalyticsConstants
import tv.anilibria.module.data.analytics.features.AuthMainAnalytics
import tv.anilibria.module.data.analytics.features.AuthSocialAnalytics
import tv.anilibria.module.domain.errors.EmptyFieldException
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
@InjectViewState
class AuthPresenter @Inject constructor(
    private val router: Router,
    private val systemMessenger: SystemMessenger,
    private val authRepository: AuthRepository,
    private val errorHandler: IErrorHandler,
    private val authMainAnalytics: AuthMainAnalytics,
    private val authSocialAnalytics: AuthSocialAnalytics,
    private val authStateHolder: AuthStateHolder,
    private val appLinkHelper: AppLinkHelper
) : BasePresenter<AuthView>(router) {

    private var currentLogin = ""
    private var currentPassword = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewModelScope.launch {
            authRepository.loadSocialAuth()
        }

        authRepository
            .observeSocialAuth()
            .onEach { viewState.showSocial(it.map { it.toState() }) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)

        updateButtonState()
    }

    fun onSocialClick(item: SocialAuthItemState) {
        authMainAnalytics.socialClick(item.key)
        authSocialAnalytics.open(AnalyticsConstants.screen_auth_main)
        router.navigateTo(Screens.AuthSocial(item.key))
    }

    fun setLogin(login: String) {
        currentLogin = login
        updateButtonState()
    }

    fun setPassword(password: String) {
        currentPassword = password
        updateButtonState()
    }

    private fun updateButtonState() {
        val enabled = currentLogin.isNotEmpty() && currentPassword.isNotEmpty()
        viewState.setSignButtonEnabled(enabled)
    }

    fun signIn() {
        authMainAnalytics.loginClick()
        viewState.setRefreshing(true)
        viewModelScope.launch {
            runCatching {
                authRepository.signIn(currentLogin, currentPassword, "")
            }.onSuccess {
                viewState.setRefreshing(false)
                decideWhatToDo(authStateHolder.get())
            }.onFailure {
                viewState.setRefreshing(false)
                if (isEmpty2FaCode(it)) {
                    router.navigateTo(Screens.Auth2FaCode(currentLogin, currentPassword))
                } else {
                    authMainAnalytics.error(it)
                    errorHandler.handle(it)
                }
            }
        }

    }

    private fun isEmpty2FaCode(error: Throwable): Boolean {
        return currentLogin.isNotEmpty()
                && currentPassword.isNotEmpty()
                && error is EmptyFieldException
    }

    private fun decideWhatToDo(state: AuthState) {
        if (state == AuthState.AUTH) {
            authMainAnalytics.success()
            router.finishChain()
        } else {
            authMainAnalytics.wrongSuccess()
            systemMessenger.showMessage("Что-то пошло не так")
        }
    }

    fun skip() {
        viewModelScope.launch {
            authMainAnalytics.skipClick()
            authStateHolder.skip()
            router.finishChain()
        }
    }

    fun registrationClick() {
        authMainAnalytics.regClick()
        viewState.showRegistrationDialog()
    }

    fun registrationToSiteClick() {
        authMainAnalytics.regToSiteClick()
        viewModelScope.launch {
            appLinkHelper.openLink(RelativeUrl("/pages/login.php"))
        }
    }

    fun submitUseTime(time: Long) {
        authMainAnalytics.useTime(time)
    }

}
