package tv.anilibria.feature.data.analytics

import toothpick.InjectConstructor
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.player.data.PlayerPreferencesStorage
import tv.anilibria.feature.data.analytics.features.mapper.*
import tv.anilibria.feature.data.preferences.PreferencesStorage

@InjectConstructor
class AnalyticsProfileDataSource(
    private val preferencesStorage: PreferencesStorage,
    private val playerPreferencesStorage: PlayerPreferencesStorage,
    private val apiConfig: ApiConfigController,
    private val userHolder: AuthStateHolder,
) {

    suspend fun getApiAddressTag(): String {
        return apiConfig.getActive().tag
    }

    suspend fun getAppTheme(): String {
        return preferencesStorage.appTheme.get().toAnalyticsAppTheme().value
    }

    suspend fun getQualitySettings(): String {
        return playerPreferencesStorage.quality.get().toAnalyticsQuality().value
    }

    suspend fun getPlayerSettings(): String {
        return playerPreferencesStorage.playerType.get().toAnalyticsPlayer().value
    }

    suspend fun getPipSettings(): String {
        return playerPreferencesStorage.pipControl.get().toAnalyticsPip().value
    }

    suspend fun getPlaySpeedSettings(): Float {
        return playerPreferencesStorage.playSpeed.get()
    }

    suspend fun getNotificationsAllSettings(): Boolean {
        return preferencesStorage.notificationsAll.get()
    }

    suspend fun getNotificationsServiceSettings(): Boolean {
        return preferencesStorage.notificationsService.get()
    }

    suspend fun getEpisodeOrderSettings(): Boolean {
        return preferencesStorage.episodesIsReverse.get()
    }

    suspend fun getAuthState(): String {
        return userHolder.get().toAnalyticsAuthState().value
    }

}