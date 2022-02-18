package tv.anilibria.module.data.analytics

import toothpick.InjectConstructor
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.module.data.AuthStateHolder
import tv.anilibria.module.data.analytics.features.mapper.*
import tv.anilibria.module.data.preferences.PreferencesStorage

@InjectConstructor
class AnalyticsProfileDataSource(
    private val preferencesHolder: PreferencesStorage,
    private val apiConfig: ApiConfigController,
    private val userHolder: AuthStateHolder,
) {

    suspend fun getApiAddressTag(): String {
        return apiConfig.getActive().tag
    }

    suspend fun getAppTheme(): String {
        return preferencesHolder.appTheme.get().toAnalyticsAppTheme().value
    }

    suspend fun getQualitySettings(): String {
        return preferencesHolder.quality.get().toAnalyticsQuality().value
    }

    suspend fun getPlayerSettings(): String {
        return preferencesHolder.playerType.get().toAnalyticsPlayer().value
    }

    suspend fun getPipSettings(): String {
        return preferencesHolder.pipControl.get().toAnalyticsPip().value
    }

    suspend fun getPlaySpeedSettings(): Float {
        return preferencesHolder.playSpeed.get()
    }

    suspend fun getNotificationsAllSettings(): Boolean {
        return preferencesHolder.notificationsAll.get()
    }

    suspend fun getNotificationsServiceSettings(): Boolean {
        return preferencesHolder.notificationsService.get()
    }

    suspend fun getEpisodeOrderSettings(): Boolean {
        return preferencesHolder.episodesIsReverse.get()
    }

    suspend fun getAuthState(): String {
        return userHolder.get().toAnalyticsAuthState().value
    }

}