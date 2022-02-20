package ru.radiationx.shared_app.analytics.profile

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.module.data.analytics.AnalyticsProfileDataSource
import tv.anilibria.module.data.analytics.ProfileConstants
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile

@InjectConstructor
class LoggingAnalyticsProfile(
    private val dataSource: AnalyticsProfileDataSource
) : AnalyticsProfile {

    override fun update() {
        GlobalScope.launch {
            try {
                unsafeUpdate()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private suspend fun unsafeUpdate() {
        val singleSources = with(dataSource) {
            listOf<Pair<String, Any>>(
                getApiAddressTag().mapToAttr(ProfileConstants.address_tag),
                getAppTheme().mapToAttr(ProfileConstants.app_theme),
                getQualitySettings().mapToAttr(ProfileConstants.quality),
                getPlayerSettings().mapToAttr(ProfileConstants.player),
                getPipSettings().mapToAttr(ProfileConstants.pip),
                getPlaySpeedSettings().mapToAttr(ProfileConstants.play_speed),
                getNotificationsAllSettings().mapToAttr(ProfileConstants.notification_all),
                getNotificationsServiceSettings().mapToAttr(ProfileConstants.notification_service),
                getEpisodeOrderSettings().mapToAttr(ProfileConstants.episode_order),
                getAuthState().mapToAttr(ProfileConstants.auth_state),
            )
        }

        Log.d("LoggingAnalyticsProfile", singleSources.toMap().toString())
    }

    private fun Any.mapToAttr(name: String): Pair<String, Any> = Pair(name, this)
}