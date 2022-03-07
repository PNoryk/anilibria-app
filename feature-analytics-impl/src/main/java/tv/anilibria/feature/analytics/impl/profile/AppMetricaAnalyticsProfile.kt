package tv.anilibria.feature.analytics.impl.profile

import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.profile.Attribute
import com.yandex.metrica.profile.UserProfile
import com.yandex.metrica.profile.UserProfileUpdate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import toothpick.InjectConstructor
import tv.anilibria.feature.analytics.impl.AnalyticsProfileDataSource
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
                getApiAddressTag().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.address_tag),
                getAppTheme().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.app_theme),
                getQualitySettings().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.quality),
                getPlayerSettings().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.player),
                getPipSettings().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.pip),
                getPlaySpeedSettings().mapFloatAttr(tv.anilibria.feature.analytics.api.ProfileConstants.play_speed),
                getNotificationsAllSettings().mapBoolAttr(tv.anilibria.feature.analytics.api.ProfileConstants.notification_all),
                getNotificationsServiceSettings().mapBoolAttr(tv.anilibria.feature.analytics.api.ProfileConstants.notification_service),
                getEpisodeOrderSettings().mapBoolAttr(tv.anilibria.feature.analytics.api.ProfileConstants.episode_order),
                getAuthState().mapStringAttr(tv.anilibria.feature.analytics.api.ProfileConstants.auth_state),
            )
        }

        val profile = singleSources.let { attributes ->
            UserProfile.newBuilder().run {
                attributes.forEach { attribute ->
                    apply(attribute)
                }
                build()
            }
        }
        YandexMetrica.reportUserProfile(profile)
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