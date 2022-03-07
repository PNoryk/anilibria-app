package ru.radiationx.anilibria.screen.player.speed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.feature.player.data.PlayerPreferencesStorage

@InjectConstructor
class PlayerSpeedViewModel(
    private val playerPreferencesStorage: PlayerPreferencesStorage,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    private val speedList = listOf(0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)

    val speedData = MutableLiveData<List<String>>()
    val selectedIndex = MutableLiveData<Int>()

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            speedData.value = speedList.map {
                if (it == 1.0f) {
                    "Обычная"
                } else {
                    "${it}x"
                }
            }
            selectedIndex.value = speedList.indexOf(playerPreferencesStorage.playSpeed.get())
        }
    }

    fun applySpeed(index: Int) {
        viewModelScope.launch {
            playerPreferencesStorage.playSpeed.put(speedList[index])
            guidedRouter.close()
        }
    }
}