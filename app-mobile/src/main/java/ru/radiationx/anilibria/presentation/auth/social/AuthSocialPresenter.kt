package ru.radiationx.anilibria.presentation.auth.social

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.common.webpage.WebPageViewState
import ru.radiationx.anilibria.ui.fragments.auth.social.AuthSocialScreenState
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.auth.data.AuthRepository
import tv.anilibria.feature.auth.data.domain.SocialAuthService
import tv.anilibria.module.data.analytics.features.AuthSocialAnalytics
import tv.anilibria.module.domain.errors.SocialAuthException
import javax.inject.Inject

@InjectViewState
class AuthSocialPresenter @Inject constructor(
    private val authRepository: AuthRepository,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val authSocialAnalytics: AuthSocialAnalytics
) : BasePresenter<AuthSocialView>(router) {

    var argKey: String = ""

    private var currentData: SocialAuthService? = null

    private val detector = WebAuthSoFastDetector()
    private var currentSuccessUrl: String? = null

    private val stateController = StateController(
        AuthSocialScreenState(
            pageState = WebPageViewState.Loading
        )
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        resetPage()
    }

    private fun resetPage() {
        viewModelScope.launch {
            runCatching {
                authRepository.getSocialAuth(argKey)
            }.onSuccess {
                currentData = it
                detector.loadUrl(it.socialUrl)
                viewState.loadPage(it)
            }.onFailure {
                authSocialAnalytics.error(it)
                errorHandler.handle(it)
            }
        }
    }

    fun onClearDataClick() {
        currentSuccessUrl = null
        detector.reset()
        detector.clearCookies()
        resetPage()
        stateController.updateState {
            it.copy(showClearCookies = false)
        }
    }

    fun onContinueClick() {
        stateController.updateState {
            it.copy(showClearCookies = false)
        }
        currentSuccessUrl?.also { signSocial(it) }
    }

    fun submitUseTime(time: Long) {
        authSocialAnalytics.useTime(time)
    }

    fun onSuccessAuthResult(result: String) {
        if (detector.isSoFast()) {
            currentSuccessUrl = result
            stateController.updateState {
                it.copy(showClearCookies = true)
            }
        } else {
            signSocial(result)
        }
    }

    fun onUserUnderstandWhatToDo() {
        router.exit()
    }

    fun sendAnalyticsPageError(error: Exception) {
        authSocialAnalytics.error(error)
    }

    fun onPageStateChanged(pageState: WebPageViewState) {
        stateController.updateState {
            it.copy(pageState = pageState)
        }
    }

    private fun signSocial(resultUrl: String) {
        val model = currentData ?: return

        stateController.updateState {
            it.copy(isAuthProgress = true)
        }
        viewModelScope.launch {
            runCatching {
                authRepository.signInSocial(resultUrl, model)
            }.onSuccess {
                stateController.updateState {
                    it.copy(isAuthProgress = true)
                }
                authSocialAnalytics.success()
                router.finishChain()
            }.onFailure {
                stateController.updateState {
                    it.copy(isAuthProgress = true)
                }
                authSocialAnalytics.error(it)
                if (it is SocialAuthException) {
                    viewState.showError()
                } else {
                    errorHandler.handle(it)
                    router.exit()
                }
            }
        }
    }

}