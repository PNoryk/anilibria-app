package ru.radiationx.shared_app.analytics.profile

import toothpick.InjectConstructor
import tv.anilibria.plugin.data.analytics.profile.AnalyticsProfile

@InjectConstructor
class CombinedAnalyticsProfile(
    private val appMetrica: AppMetricaAnalyticsProfile,
    private val logging: LoggingAnalyticsProfile
) : AnalyticsProfile {

    override fun update() {
        logging.update()
        appMetrica.update()
    }
}