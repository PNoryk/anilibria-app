package ru.radiationx.anilibria.presentation.comments

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.AuthVkNotifier
import ru.radiationx.anilibria.model.loading.DataLoadingController
import ru.radiationx.anilibria.model.loading.ScreenStateAction
import ru.radiationx.anilibria.model.loading.StateController
import ru.radiationx.anilibria.navigation.Screens
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.ui.common.webpage.WebPageViewState
import ru.radiationx.anilibria.ui.fragments.comments.VkCommentsScreenState
import ru.radiationx.anilibria.ui.fragments.comments.VkCommentsState
import ru.terrakok.cicerone.Router
import tv.anilibria.core.types.AbsoluteUrl
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.vkcomments.data.VkCommentsRepository
import tv.anilibria.feature.content.data.ReleaseInteractor
import tv.anilibria.feature.content.data.analytics.AnalyticsConstants
import tv.anilibria.feature.content.data.analytics.features.AuthVkAnalytics
import tv.anilibria.feature.content.data.analytics.features.CommentsAnalytics
import tv.anilibria.feature.domain.entity.release.ReleaseCode
import tv.anilibria.feature.domain.entity.release.ReleaseId
import javax.inject.Inject

@InjectViewState
class VkCommentsPresenter @Inject constructor(
    private val authStateHolder: AuthStateHolder,
    private val vkCommentsRepository: VkCommentsRepository,
    private val releaseInteractor: ReleaseInteractor,
    private val authHolder: AuthVkNotifier,
    private val router: Router,
    private val errorHandler: IErrorHandler,
    private val authVkAnalytics: AuthVkAnalytics,
    private val commentsAnalytics: CommentsAnalytics
) : BasePresenter<VkCommentsView>(router) {

    var releaseId: ReleaseId? = null
    var releaseIdCode: ReleaseCode? = null

    private var isVisibleToUser = false
    private var pendingAuthRequest: AbsoluteUrl? = null

    private var hasJsError = false
    private var jsErrorClosed = false

    private var hasVkBlockedError = false
    private var vkBlockedErrorClosed = false

    private val loadingController = DataLoadingController(viewModelScope) {
        getDataSource().let { ScreenStateAction.Data(it, false) }
    }

    private val stateController = StateController(
        VkCommentsScreenState(
            pageState = WebPageViewState.Loading
        )
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        authStateHolder
            .observe()
            .distinctUntilChanged()
            .onEach { viewState.pageReloadAction() }
            .launchIn(viewModelScope)

        authHolder.observeVkAuthChange()
            .onEach { viewState.pageReloadAction() }
            .launchIn(viewModelScope)

        stateController
            .observeState()
            .onEach { viewState.showState(it) }
            .launchIn(viewModelScope)

        loadingController
            .observeState()
            .onEach { loadingData ->
                stateController.updateState {
                    it.copy(data = loadingData)
                }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            runCatching {
                vkCommentsRepository.checkVkBlocked()
            }.onSuccess { vkBlocked ->
                hasVkBlockedError = vkBlocked
                updateVkBlockedState()
            }.onFailure {
                it.printStackTrace()
            }
        }

        loadingController.refresh()
    }

    fun refresh() {
        loadingController.refresh()
    }

    fun pageReload() {
        viewState.pageReloadAction()
    }

    fun setVisibleToUser(isVisible: Boolean) {
        isVisibleToUser = isVisible
        tryExecutePendingAuthRequest()
    }

    fun authRequest(url: AbsoluteUrl) {
        pendingAuthRequest = url
        tryExecutePendingAuthRequest()
    }

    fun onPageLoaded() {
        commentsAnalytics.loaded()
    }

    fun onPageCommitError(error: Exception) {
        commentsAnalytics.error(error)
    }

    fun notifyNewJsError() {
        hasJsError = true
        updateJsErrorState()
    }

    fun closeJsError() {
        jsErrorClosed = true
        updateJsErrorState()
    }

    fun closeVkBlockedError() {
        vkBlockedErrorClosed = true
        updateVkBlockedState()
    }

    fun onNewPageState(pageState: WebPageViewState) {
        stateController.updateState {
            it.copy(pageState = pageState)
        }
    }

    private fun updateJsErrorState() {
        stateController.updateState {
            it.copy(jsErrorVisible = hasJsError && !jsErrorClosed)
        }
    }

    private fun updateVkBlockedState() {
        stateController.updateState {
            it.copy(vkBlockedVisible = hasVkBlockedError && !vkBlockedErrorClosed)
        }
    }

    private fun tryExecutePendingAuthRequest() {
        viewModelScope.launch {
            val url = pendingAuthRequest
            if (isVisibleToUser && url != null) {
                pendingAuthRequest = null
                authVkAnalytics.open(AnalyticsConstants.screen_auth_vk)
                router.navigateTo(Screens.Auth(Screens.AuthVk(url)))
            }
        }
    }

    private suspend fun getDataSource(): VkCommentsState {
        return try {
            val commentsSource = vkCommentsRepository.getComments()
            val releaseSource = releaseInteractor.getItem(releaseId, releaseIdCode)
                ?: releaseInteractor.loadRelease(releaseId, releaseIdCode).first()
            VkCommentsState(
                url = "${commentsSource.baseUrl}release/${releaseSource.code?.code}.html",
                script = commentsSource.script
            )
        } catch (ex: Exception) {
            commentsAnalytics.error(ex)
            errorHandler.handle(ex)
            throw ex
        }
    }
}