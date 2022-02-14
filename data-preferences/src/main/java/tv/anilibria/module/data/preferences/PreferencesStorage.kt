package tv.anilibria.module.data.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import tv.anilibria.plugin.data.storage.*
import javax.inject.Inject

// todo переделать на нормальную подписку обновлений
class PreferencesStorage @Inject constructor(
    private val storage: DataStorage
) {

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
        private const val APP_THEME_KEY = "app_theme_dark"
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
        storageBooleanKey(NEW_DONATION_REMIND_KEY, true),
        storage
    )

    private val releaseRemindHolder = StorageDataHolder(
        storageBooleanKey(RELEASE_REMIND_KEY, true),
        storage
    )

    private val searchRemindHolder = StorageDataHolder(
        storageBooleanKey(SEARCH_REMIND_KEY, true),
        storage
    )

    private val episodesReverseHolder = StorageDataHolder(
        storageBooleanKey(EPISODES_IS_REVERSE_KEY, false),
        storage
    )

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

    private val notificationsAllHolder = StorageDataHolder(
        storageBooleanKey(NOTIFICATIONS_ALL_KEY, true),
        storage
    )

    private val notificationsServiceHolder = StorageDataHolder(
        storageBooleanKey(NOTIFICATIONS_SERVICE_KEY, true),
        storage
    )

    private val appThemeHolder = ModelStorageDataHolder(
        storageBooleanKey(APP_THEME_KEY, false),
        storage,
        read = { if (it) AppTheme.DARK else AppTheme.LIGHT },
        write = { it == AppTheme.DARK }
    )

    val newDonationRemind = ObservableData(donationRemindHolder)

    val releaseRemind = ObservableData(releaseRemindHolder)

    val searchRemind = ObservableData(searchRemindHolder)

    val episodesIsReverse = ObservableData(episodesReverseHolder)

    val quality = ObservableData(qualityHolder)

    val playerType = ObservableData(playerTypeHolder)

    val playSpeed = ObservableData(playSpeedHolder)

    val pipControl = ObservableData(pipModeHolder)

    val notificationsAll = ObservableData(notificationsAllHolder)

    val notificationsService = ObservableData(notificationsServiceHolder)

    val appTheme = ObservableData(appThemeHolder)
}


