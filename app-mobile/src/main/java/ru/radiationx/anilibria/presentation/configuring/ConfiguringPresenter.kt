package ru.radiationx.anilibria.presentation.configuring

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.radiationx.anilibria.presentation.common.BasePresenter
import ru.radiationx.anilibria.presentation.common.IErrorHandler
import ru.terrakok.cicerone.Router
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.ConfiguringInteractor

@InjectViewState
@InjectConstructor
class ConfiguringPresenter(
    private val router: Router,
    private val configuringInteractor: ConfiguringInteractor,
    private val errorHandler: IErrorHandler
) : BasePresenter<ConfiguringView>(router) {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        configuringInteractor
            .observeScreenState()
            .onEach { viewState.updateScreen(it) }
            .catch { errorHandler.handle(it) }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            configuringInteractor.initCheck()
        }
    }

    fun continueCheck() {
        viewModelScope.launch {
            configuringInteractor.repeatCheck()
        }
    }

    fun nextCheck() {
        viewModelScope.launch {
            configuringInteractor.nextCheck()
        }
    }

    fun skipCheck() {
        viewModelScope.launch {
            configuringInteractor.skipCheck()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch {
            configuringInteractor.finishCheck()
        }
    }
}