package tv.anilibria.feature.analytics.impl.profile

import android.util.Log
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.profile.Attribute
import com.yandex.metrica.profile.UserProfile
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
        val attributes = dataSource
            .getAttributes()
            .mapNotNull { entry ->
                when (val value = entry.value) {
                    is String -> value.mapStringAttr(entry.key)
                    is Int -> value.mapIntAttr(entry.key)
                    is Float -> value.mapFloatAttr(entry.key)
                    is Boolean -> value.mapBoolAttr(entry.key)
                    else -> {
                        Log.e(
                            "AppMetricaAnalytics",
                            "Unknown type for ${entry.key} = ${entry.value}"
                        )
                        null
                    }
                }
            }

        val profile = UserProfile.newBuilder().run {
            attributes.forEach { attribute ->
                apply(attribute)
            }
            build()
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