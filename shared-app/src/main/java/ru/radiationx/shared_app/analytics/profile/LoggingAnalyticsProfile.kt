package ru.radiationx.shared_app.analytics.profile

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.api.AnalyticsProfileDataSource
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
                getApiAddressTag().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.address_tag),
                getAppTheme().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.app_theme),
                getQualitySettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.quality),
                getPlayerSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.player),
                getPipSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.pip),
                getPlaySpeedSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.play_speed),
                getNotificationsAllSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.notification_all),
                getNotificationsServiceSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.notification_service),
                getEpisodeOrderSettings().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.episode_order),
                getAuthState().mapToAttr(tv.anilibria.feature.analytics.api.ProfileConstants.auth_state),
            )
        }

        Log.d("LoggingAnalyticsProfile", singleSources.toMap().toString())
    }

    private fun Any.mapToAttr(name: String): Pair<String, Any> = Pair(name, this)
}