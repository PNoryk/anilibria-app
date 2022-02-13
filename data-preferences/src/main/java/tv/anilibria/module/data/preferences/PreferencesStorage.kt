package tv.anilibria.module.data.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tv.anilibria.plugin.data.storage.*
import javax.inject.Inject

/**
 * Created by radiationx on 03.02.18.
 */
class PreferencesStorage @Inject constructor(
    private val storage: DataStorage
) : PreferencesHolder {

    companion object {
        private const val NEW_DONATION_REMIND_KEY = "new_donation_remind"
        private const val RELEASE_REMIND_KEY = "release_remind"
        private const val SEARCH_REMIND_KEY = "search_remind"
        private const val EPISODES_IS_REVERSE_KEY = "episodes_is_reverse"
        private const val QUALITY_KEY = "quality"
        private const val PLAYER_TYPE_KEY = "player_type"
        private const val PLAY_SPEED_KEY = "play_speed"
        private const val PIP_CONTROL_KEY = "pip_control"
        private const val NOTIFICATIONS_ALL_KEY = "notifications.all"
        private const val NOTIFICATIONS_SERVICE_KEY = "notifications.service"
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
                NOTIFICATIONS_ALL_KEY -> notificationsAll.triggerUpdate()
                NOTIFICATIONS_SERVICE_KEY -> notificationsService.triggerUpdate()
                QUALITY_KEY -> quality.triggerUpdate()
                PLAY_SPEED_KEY -> playSpeed.triggerUpdate()
                SEARCH_REMIND_KEY -> searchRemind.triggerUpdate()
                RELEASE_REMIND_KEY -> releaseRemind.triggerUpdate()
                EPISODES_IS_REVERSE_KEY -> episodesIsReverse.triggerUpdate()
                NEW_DONATION_REMIND_KEY -> newDonationRemind.triggerUpdate()
                PLAYER_TYPE_KEY -> playerType.triggerUpdate()
                PIP_CONTROL_KEY -> pipControl.triggerUpdate()
            }
        }.launchIn(observerScope)
    }

    fun stopObserveUpdates() {
        observerJob?.cancel()
    }

    private val donationRemindHolder = StorageDataHolder(
        storageBooleanKey(NEW_DONATION_REMIND_KEY),
        storage
    )

    private val releaseRemindHolder = StorageDataHolder(
        storageBooleanKey(RELEASE_REMIND_KEY),
        storage
    )

    private val searchRemindHolder = StorageDataHolder(
        storageBooleanKey(SEARCH_REMIND_KEY),
        storage
    )

    private val episodesReverseHolder = StorageDataHolder(
        storageBooleanKey(EPISODES_IS_REVERSE_KEY),
        storage
    )

    private val qualityHolder = ModelStorageDataHolder(
        storageIntKey(QUALITY_KEY),
        storage,
        read = { it?.toPlayerQuality() },
        write = { it?.toPrefs() }
    )

    private val playerTypeHolder = ModelStorageDataHolder(
        storageIntKey(PLAYER_TYPE_KEY),
        storage,
        read = { it?.toPlayerType() },
        write = { it?.toPrefs() }
    )

    private val playSpeedHolder = StorageDataHolder(
        storageFloatKey(PLAY_SPEED_KEY),
        storage
    )

    private val pipModeHolder = ModelStorageDataHolder(
        storageIntKey(PIP_CONTROL_KEY),
        storage,
        read = { it?.toPipMode() },
        write = { it?.toPrefs() }
    )

    private val notificationsAllHolder = StorageDataHolder(
        storageBooleanKey(NOTIFICATIONS_ALL_KEY),
        storage
    )

    private val notificationsServiceHolder = StorageDataHolder(
        storageBooleanKey(NOTIFICATIONS_SERVICE_KEY),
        storage
    )

    override val newDonationRemind: ObservableData<Boolean> = ObservableData(donationRemindHolder)

    override val releaseRemind: ObservableData<Boolean> = ObservableData(releaseRemindHolder)

    override val searchRemind: ObservableData<Boolean> = ObservableData(searchRemindHolder)

    override val episodesIsReverse: ObservableData<Boolean> = ObservableData(episodesReverseHolder)

    override val quality: ObservableData<PlayerQuality> = ObservableData(qualityHolder)

    override val playerType: ObservableData<PlayerType> = ObservableData(playerTypeHolder)

    override val playSpeed: ObservableData<Float> = ObservableData(playSpeedHolder)

    override val pipControl: ObservableData<PlayerPipMode> = ObservableData(pipModeHolder)

    override val notificationsAll: ObservableData<Boolean> = ObservableData(notificationsAllHolder)

    override val notificationsService: ObservableData<Boolean> =
        ObservableData(notificationsServiceHolder)
}


