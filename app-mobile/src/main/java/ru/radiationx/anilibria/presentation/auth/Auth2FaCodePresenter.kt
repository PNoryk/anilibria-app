package ru.radiationx.anilibria.presentation.auth

import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.auth.data.AuthRepository
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.auth.data.domain.AuthState
import tv.anilibria.module.data.analytics.features.AuthMainAnalytics
import tv.anilibria.module.domain.errors.WrongPasswordException
import javax.inject.Inject

/**
 * Created by radiationx on 30.12.17.
 */
@InjectViewState
class Auth2FaCodePresenter @Inject constructor(
    private val router: Router,
    private val systemMessenger: SystemMessenger,
    private val authRepository: AuthRepository,
    private val errorHandler: IErrorHandler,
    private val authMainAnalytics: AuthMainAnalytics,
    private val authStateHolder: AuthStateHolder
) : BasePresenter<Auth2FaCodeView>(router) {

    var currentLogin = ""
    var currentPassword = ""
    private var currentCode2fa = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateButtonState()
    }

    fun setCode2fa(code2fa: String) {
        currentCode2fa = code2fa
        updateButtonState()
    }

    private fun updateButtonState() {
        val enabled = currentCode2fa.isNotEmpty()
        viewState.setSignButtonEnabled(enabled)
    }

    fun signIn() {
        viewState.setRefreshing(true)
        viewModelScope.launch {
            runCatching {
                authRepository.signIn(currentLogin, currentPassword, currentCode2fa)
            }.onSuccess {
                viewState.setRefreshing(false)
                decideWhatToDo(authStateHolder.get())
            }.onFailure {
                viewState.setRefreshing(false)
                authMainAnalytics.error(it)
                errorHandler.handle(it)
                if (it is WrongPasswordException) {
                    router.exit()
                }
            }
        }
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
}
