package ru.radiationx.shared_app.analytics.profile

import com.yandex.metrica.profile.Attribute
import com.yandex.metrica.profile.UserProfile
import com.yandex.metrica.profile.UserProfileUpdate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.feature.content.data.analytics.AnalyticsProfileDataSource
import tv.anilibria.feature.content.data.analytics.ProfileConstants
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile

@InjectConstructor
class AppMetricaAnalyticsProfile(
    private val dataSource: AnalyticsProfileDataSource
) : AnalyticsProfile {

    override fun update() {
        GlobalScope.launch {
            try {
                unsafeUpdate()
            } catch (ex: Throwable) {
                ex.printStackTrace()
            }
        }
    }

    private suspend fun unsafeUpdate() {
        val singleSources = with(dataSource) {
            listOf<UserProfileUpdate<*>>(
                getApiAddressTag().mapStringAttr(ProfileConstants.address_tag),
                getAppTheme().mapStringAttr(ProfileConstants.app_theme),
                getQualitySettings().mapStringAttr(ProfileConstants.quality),
                getPlayerSettings().mapStringAttr(ProfileConstants.player),
                getPipSettings().mapStringAttr(ProfileConstants.pip),
                getPlaySpeedSettings().mapFloatAttr(ProfileConstants.play_speed),
                getNotificationsAllSettings().mapBoolAttr(ProfileConstants.notification_all),
                getNotificationsServiceSettings().mapBoolAttr(ProfileConstants.notification_service),
                getEpisodeOrderSettings().mapBoolAttr(ProfileConstants.episode_order),
                getAuthState().mapStringAttr(ProfileConstants.auth_state),
            )
        }

        singleSources.let { attributes ->
            UserProfile.newBuilder().run {
                attributes.forEach { attribute ->
                    apply(attribute)
                }
                build()
            }
        }
    }

    private fun String.mapStringAttr(name: String) = this
        .let { Attribute.customString(name).withValue(it) }

    private fun Int.mapIntAttr(name: String) = this
        .let { Attribute.customNumber(name).withValue(it.toDouble()) }

    private fun Float.mapFloatAttr(name: String) = this
        .let { Attribute.customNumber(name).withValue(it.toDouble()) }

    private fun Boolean.mapBoolAttr(name: String) = this
        .let { Attribute.customBoolean(name).withValue(it) }
}