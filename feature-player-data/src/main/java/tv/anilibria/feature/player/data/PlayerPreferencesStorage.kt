package tv.anilibria.feature.player.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import toothpick.InjectConstructor
import tv.anilibria.feature.player.data.prefs.*
import tv.anilibria.plugin.data.storage.*

// todo переделать на нормальную подписку обновлений
@InjectConstructor
class PlayerPreferencesStorage(
    private val storage: DataStorage
) {

    companion object {
        private const val QUALITY_KEY = "quality"
        private const val PLAYER_TYPE_KEY = "player_type"
        private const val PLAY_SPEED_KEY = "play_speed"
        private const val PIP_CONTROL_KEY = "pip_control"
    }

    private val observerScope = CoroutineScope(SupervisorJob())
    private var observerJob: Job? = null

    init {
        startObserveUpdates()
    }

    fun startObserveUpdates() {
        stopObserveUpdates()
        observerJob = storage.observeAllUpdates().onEach { key ->
            when (key) {
                QUALITY_KEY -> quality.triggerUpdate()
                PLAY_SPEED_KEY -> playSpeed.triggerUpdate()
                PLAYER_TYPE_KEY -> playerType.triggerUpdate()
                PIP_CONTROL_KEY -> pipControl.triggerUpdate()
            }
        }.launchIn(observerScope)
    }

    fun stopObserveUpdates() {
        observerJob?.cancel()
    }

    private val qualityHolder = ModelStorageDataHolder(
        storageIntKey(QUALITY_KEY, PrefsConstants.QUALITY_NO),
        storage,
        read = { it.toPlayerQuality() },
        write = { it.toPrefs() }
    )

    private val playerTypeHolder = ModelStorageDataHolder(
        storageIntKey(PLAYER_TYPE_KEY, PrefsConstants.PLAYER_TYPE_NO),
        storage,
        read = { it.toPlayerType() },
        write = { it.toPrefs() }
    )

    private val playSpeedHolder = StorageDataHolder(
        storageFloatKey(PLAY_SPEED_KEY, PrefsConstants.DEFAULT_PLAYER_SPEED),
        storage
    )

    private val pipModeHolder = ModelStorageDataHolder(
        storageIntKey(PIP_CONTROL_KEY, PrefsConstants.PIP_BUTTON),
        storage,
        read = { it.toPipMode() },
        write = { it.toPrefs() }
    )

    val quality = ObservableData(qualityHolder)

    val playerType = ObservableData(playerTypeHolder)

    val playSpeed = ObservableData(playSpeedHolder)

    val pipControl = ObservableData(pipModeHolder)
}


