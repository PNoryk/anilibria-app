package ru.radiationx.shared_app.analytics.profile

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.AnalyticsProfileDataSource
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
                getApiAddressTag().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.address_tag),
                getAppTheme().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.app_theme),
                getQualitySettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.quality),
                getPlayerSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.player),
                getPipSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.pip),
                getPlaySpeedSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.play_speed),
                getNotificationsAllSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.notification_all),
                getNotificationsServiceSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.notification_service),
                getEpisodeOrderSettings().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.episode_order),
                getAuthState().mapToAttr(tv.anilibria.feature.analytics.ProfileConstants.auth_state),
            )
        }

        Log.d("LoggingAnalyticsProfile", singleSources.toMap().toString())
    }

    private fun Any.mapToAttr(name: String): Pair<String, Any> = Pair(name, this)
}