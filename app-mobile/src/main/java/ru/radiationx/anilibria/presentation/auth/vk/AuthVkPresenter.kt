package ru.radiationx.anilibria.presentation.auth.vk

import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.AuthVkNotifier
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.presentation.auth.social.WebAuthSoFastDetector
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.ui.common.webpage.WebPageViewState
import ru.radiationx.anilibria.ui.fragments.auth.vk.AuthVkScreenState
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.core.types.AbsoluteUrl

@InjectViewState
@InjectConstructor
class AuthVkPresenter(
    private val authHolder: AuthVkNotifier,
    private val router: Router
) : BasePresenter<AuthVkView>(router) {

    private var resultPattern =
        "(\\?act=widget|anilibria\\.tv\\/public\\/vk\\.php\\?code=|vk\\.com\\/widget_comments\\.php)"

    lateinit var argUrl: AbsoluteUrl

    private val detector = WebAuthSoFastDetector()
    private var currentSuccessUrl: String? = null

    private val stateController = StateController(
        AuthVkScreenState(
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
        detector.loadUrl(argUrl)
        viewState.loadPage(argUrl, resultPattern)
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
        currentSuccessUrl?.also { successSignVk(it) }
    }

    fun onSuccessAuthResult(result: String) {
        if (detector.isSoFast()) {
            currentSuccessUrl = result
            stateController.updateState {
                it.copy(showClearCookies = true)
            }
        } else {
            successSignVk(result)
        }
    }

    fun onPageStateChanged(pageState: WebPageViewState) {
        stateController.updateState {
            it.copy(pageState = pageState)
        }
    }

    private fun successSignVk(resultUrl: String) {
        viewModelScope.launch {
            authHolder.changeVkAuth(true)
            router.exit()
        }
    }
}