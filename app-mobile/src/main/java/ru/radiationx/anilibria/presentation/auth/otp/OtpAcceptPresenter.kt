package ru.radiationx.anilibria.presentation.auth.otp

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.terrakok.cicerone.Router
import tv.anilibria.feature.auth.data.AuthRepository
import tv.anilibria.feature.content.data.analytics.features.AuthDeviceAnalytics
import tv.anilibria.feature.domain.errors.OtpAcceptedException
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@InjectViewState
class OtpAcceptPresenter @Inject constructor(
    router: Router,
    private val authRepository: AuthRepository,
    private val errorHandler: IErrorHandler,
    private val authDeviceAnalytics: AuthDeviceAnalytics
) : BasePresenter<OtpAcceptView>(router) {

    private var success = false
    private var progress = false
    private var error: String? = null


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateState()
    }

    fun submitUseTime(time: Long) {
        authDeviceAnalytics.useTime(time)
    }

    fun onAcceptClick(code: String) {
        if (progress || success) {
            return
        }
        if (code.isBlank()) {
            error = "Поле обязательно к заполнению"
            updateState()
            return
        }
        progress = true
        updateState()
        viewModelScope.launch {
            runCatching {
                authRepository.acceptOtp(code)
            }.onSuccess {
                progress = false
                updateState()
                onSuccess()

            }.onFailure {
                progress = false
                authDeviceAnalytics.error(it)
                if (it is OtpAcceptedException) {
                    onSuccess()
                } else {
                    success = false
                    errorHandler.handle(it) { _, s ->
                        error = s.orEmpty()
                    }
                }
                updateState()
            }
        }
    }

    private fun updateState() {
        viewState.setState(success, progress, error)
    }

    private fun onSuccess() {
        authDeviceAnalytics.success()
        error = null
        success = true
        startCloseTimer()
    }

    private fun startCloseTimer() {
        viewModelScope.launch {
            delay(1500.milliseconds)
            viewState.close()
        }
    }
}