package tv.anilibria.app.mobile.preferences

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import toothpick.InjectConstructor
import tv.anilibria.plugin.data.storage.*

// todo переделать на нормальную подписку обновлений
@InjectConstructor
class PreferencesStorage(
    private val storage: DataStorage
) {

    companion object {
        private const val NEW_DONATION_REMIND_KEY = "new_donation_remind"
        private const val RELEASE_REMIND_KEY = "release_remind"
        private const val SEARCH_REMIND_KEY = "search_remind"
        private const val EPISODES_IS_REVERSE_KEY = "episodes_is_reverse"
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
                SEARCH_REMIND_KEY -> searchRemind.triggerUpdate()
                RELEASE_REMIND_KEY -> releaseRemind.triggerUpdate()
                EPISODES_IS_REVERSE_KEY -> episodesIsReverse.triggerUpdate()
                NEW_DONATION_REMIND_KEY -> newDonationRemind.triggerUpdate()
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

    val notificationsAll = ObservableData(notificationsAllHolder)

    val notificationsService = ObservableData(notificationsServiceHolder)

    val appTheme = ObservableData(appThemeHolder)
}


