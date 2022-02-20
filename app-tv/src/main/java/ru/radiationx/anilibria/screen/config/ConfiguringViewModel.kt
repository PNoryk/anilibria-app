package ru.radiationx.anilibria.screen.config

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.ConfigScreenState
import tv.anilibria.feature.networkconfig.data.ConfiguringInteractor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController

@InjectConstructor
class ConfiguringViewModel(
    private val apiConfig: ApiConfigController,
    private val configuringInteractor: ConfiguringInteractor,
) : LifecycleViewModel() {

    private var configuringStarted = false
    val screenStateData = MutableLiveData<ConfigScreenState>()
    val completeEvent = MutableLiveData<Unit>()

    fun startConfiguring() {
        if (configuringStarted) {
            return
        }
        configuringStarted = true
        apiConfig
            .observeNeedConfig()
            .onEach {
                if (!it) {
                    completeEvent.value = Unit
                }
            }
            .launchIn(viewModelScope)

        configuringInteractor
            .observeScreenState()
            .onEach {
                screenStateData.value = it
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            configuringInteractor.initCheck()
        }
    }

    fun endConfiguring() {
        //router.exit()
    }

    fun repeatCheck() {
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

    override fun onCleared() {
        super.onCleared()
        GlobalScope.launch {
            configuringInteractor.finishCheck()
        }
    }
}