package ru.radiationx.anilibria.screen.player.quality

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.radiationx.anilibria.common.fragment.GuidedRouter
import ru.radiationx.anilibria.screen.LifecycleViewModel
import toothpick.InjectConstructor
import tv.anilibria.module.data.ReleaseInteractor
import tv.anilibria.module.data.preferences.PlayerQuality
import tv.anilibria.module.data.preferences.PreferencesStorage
import tv.anilibria.module.domain.entity.release.EpisodeId

@InjectConstructor
class PlayerQualityViewModel(
    private val releaseInteractor: ReleaseInteractor,
    private val preferencesStorage: PreferencesStorage,
    private val guidedRouter: GuidedRouter
) : LifecycleViewModel() {

    companion object {
        val SD_ACTION_ID = PlayerQuality.SD.ordinal.toLong()
        val HD_ACTION_ID = PlayerQuality.HD.ordinal.toLong()
        val FULL_HD_ACTION_ID = PlayerQuality.FULL_HD.ordinal.toLong()
    }

    lateinit var argEpisodeId: EpisodeId

    val availableData = MutableLiveData<List<Long>>()
    val selectedData = MutableLiveData<Long>()

    override fun onCreate() {
        super.onCreate()

        updateAvailable()

        preferencesStorage.quality.observe()
            .onEach {
                update(it)
            }
            .launchIn(viewModelScope)
    }

    fun applyQuality(quality: Long) {
        viewModelScope.launch {
            val value = when (quality) {
                SD_ACTION_ID -> PlayerQuality.SD
                HD_ACTION_ID -> PlayerQuality.HD
                FULL_HD_ACTION_ID -> PlayerQuality.FULL_HD
                else -> PlayerQuality.SD
            }
            preferencesStorage.quality.put(value)
            guidedRouter.close()
        }
    }

    private fun updateAvailable() {
        val available = mutableListOf(SD_ACTION_ID, HD_ACTION_ID, FULL_HD_ACTION_ID)
        releaseInteractor.getFull(argEpisodeId.releaseId)?.episodes?.firstOrNull { it.id == argEpisodeId }
            ?.also {
                if (it.urlFullHd?.value.isNullOrEmpty()) {
                    available.remove(FULL_HD_ACTION_ID)
                }
                if (it.urlHd?.value.isNullOrEmpty()) {
                    available.remove(HD_ACTION_ID)
                }
                if (it.urlSd?.value.isNullOrEmpty()) {
                    available.remove(SD_ACTION_ID)
                }
            }
        availableData.value = available
    }

    private fun update(currentQuality: PlayerQuality) {
        val available = availableData.value!!
        var selectedAction = when (currentQuality) {
            PlayerQuality.SD -> SD_ACTION_ID
            PlayerQuality.HD -> HD_ACTION_ID
            PlayerQuality.FULL_HD -> FULL_HD_ACTION_ID
            else -> SD_ACTION_ID
        }

        if (selectedAction == FULL_HD_ACTION_ID && !available.contains(selectedAction)) {
            selectedAction = HD_ACTION_ID
        }
        if (selectedAction == HD_ACTION_ID && !available.contains(selectedAction)) {
            selectedAction = SD_ACTION_ID
        }
        if (selectedAction == SD_ACTION_ID && !available.contains(selectedAction)) {
            selectedAction = -1L
        }

        selectedData.value = selectedAction
    }
}