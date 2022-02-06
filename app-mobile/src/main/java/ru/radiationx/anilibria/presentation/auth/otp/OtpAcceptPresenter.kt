package ru.radiationx.anilibria.presentation.auth.otp

import io.reactivex.Single
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.radiationx.anilibria.utils.messages.SystemMessenger
import ru.radiationx.shared.ktx.SchedulersProvider
import ru.radiationx.data.analytics.features.AuthDeviceAnalytics
import ru.radiationx.data.entity.app.auth.OtpAcceptedException
import ru.radiationx.data.repository.AuthRepository
import ru.terrakok.cicerone.Router
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class OtpAcceptPresenter @Inject constructor(
    router: Router,
    private val authRepository: AuthRepository,
    private val errorHandler: IErrorHandler,
    private val messenger: SystemMessenger,
    private val schedulersProvider: SchedulersProvider,
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
        authRepository
            .acceptOtp(code)
            .doFinally {
                progress = false
                updateState()
            }
            .subscribe({
                onSuccess()
            }, {
                authDeviceAnalytics.error(it)
                if (it is OtpAcceptedException) {
                    onSuccess()
                    return@subscribe
                }
                success = false
                errorHandler.handle(it) { throwable, s ->
                    error = s.orEmpty()
                }
            })
            .addToDisposable()
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
        Single
            .timer(1500, TimeUnit.MILLISECONDS)
            .observeOn(schedulersProvider.ui())
            .subscribe({
                viewState.close()
            }, {
                authDeviceAnalytics.error(it)
                errorHandler.handle(it)
            })
            .addToDisposable()
    }
}