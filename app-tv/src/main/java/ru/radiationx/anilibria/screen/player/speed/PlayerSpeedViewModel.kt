package ru.radiationx.anilibria.screen.player.speed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.module.data.preferences.PreferencesStorage

@InjectConstructor
class PlayerSpeedViewModel(
    private val preferencesStorage: PreferencesStorage,
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
            selectedIndex.value = speedList.indexOf(preferencesStorage.playSpeed.get())
        }
    }

    fun applySpeed(index: Int) {
        viewModelScope.launch {
            preferencesStorage.playSpeed.put(speedList[index])
            guidedRouter.close()
        }
    }
}