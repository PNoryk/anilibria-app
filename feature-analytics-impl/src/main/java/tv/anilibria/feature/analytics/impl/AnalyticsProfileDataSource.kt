package tv.anilibria.feature.analytics.impl

import android.util.Log
import toothpick.InjectConstructor
import tv.anilibria.app.mobile.preferences.PreferencesStorage
import tv.anilibria.feature.analytics.api.ProfileConstants
import tv.anilibria.feature.analytics.api.features.mapper.*
import tv.anilibria.feature.auth.data.AuthStateHolder
import tv.anilibria.feature.networkconfig.data.address.ApiConfigController
import tv.anilibria.feature.player.data.PlayerPreferencesStorage

@InjectConstructor
class AnalyticsProfileDataSource(
    private val preferencesStorage: PreferencesStorage,
    private val playerPreferencesStorage: PlayerPreferencesStorage,
    private val apiConfig: ApiConfigController,
    private val userHolder: AuthStateHolder,
) {

    suspend fun getAttributes(): Map<String, Any> {
        val attributeSources = mapOf(
            ProfileConstants.address_tag to ::getApiAddressTag,
            ProfileConstants.app_theme to ::getAppTheme,
            ProfileConstants.quality to ::getQualitySettings,
            ProfileConstants.player to ::getPlayerSettings,
            ProfileConstants.pip to ::getPipSettings,
            ProfileConstants.play_speed to ::getPlaySpeedSettings,
            ProfileConstants.notification_all to ::getNotificationsAllSettings,
            ProfileConstants.notification_service to ::getNotificationsServiceSettings,
            ProfileConstants.episode_order to ::getEpisodeOrderSettings,
            ProfileConstants.auth_state to ::getAuthState,
        )
        return attributeSources
            .map { entry ->
                val value = runCatching { entry.value.invoke() }
                    .onFailure {
                        Log.e("AnalyticsProfile", "Error for attribute '${entry.key}'", it)
                    }
                    .getOrNull()
                entry.key to (value ?: "error")
            }
            .toMap()
    }


    private suspend fun getApiAddressTag(): String {
        return apiConfig.getActive().tag
    }

    private suspend fun getAppTheme(): String {
        return preferencesStorage.appTheme.get().toAnalyticsAppTheme().value
    }

    private suspend fun getQualitySettings(): String {
        return playerPreferencesStorage.quality.get().toAnalyticsQuality().value
    }

    private suspend fun getPlayerSettings(): String {
        return playerPreferencesStorage.playerType.get().toAnalyticsPlayer().value
    }

    private suspend fun getPipSettings(): String {
        return playerPreferencesStorage.pipControl.get().toAnalyticsPip().value
    }

    private suspend fun getPlaySpeedSettings(): Float {
        return playerPreferencesStorage.playSpeed.get()
    }

    private suspend fun getNotificationsAllSettings(): Boolean {
        return preferencesStorage.notificationsAll.get()
    }

    private suspend fun getNotificationsServiceSettings(): Boolean {
        return preferencesStorage.notificationsService.get()
    }

    private suspend fun getEpisodeOrderSettings(): Boolean {
        return preferencesStorage.episodesIsReverse.get()
    }

    private suspend fun getAuthState(): String {
        return userHolder.get().toAnalyticsAuthState().value
    }

}